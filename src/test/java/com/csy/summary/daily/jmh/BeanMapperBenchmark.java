package com.csy.summary.daily.jmh;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 测试结果是
 * beanCopier > orika > springBeanUtil > dozer > apacheBeanUtil
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
@Warmup(iterations = 1)
@State(Scope.Benchmark)
public class BeanMapperBenchmark {

    @Param({"100", "1000"})
    private int times;

    private int time;

    private static final MapperFactory mapperFactory;

    private static final Mapper mapper;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(SourceVO.class, TargetVO.class)
                .byDefault()
                .register();

        mapper = DozerBeanMapperBuilder.create()
                .withMappingBuilder(new BeanMappingBuilder() {
                    @Override
                    protected void configure() {
                        mapping(SourceVO.class, TargetVO.class)
                                // .fields("fullName", "name")
                                .exclude("in");
                    }
                }).build();
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(BeanMapperBenchmark.class.getName()).measurementIterations(3)
                .build();
        new Runner(options).run();
    }

    @Setup
    public void prepare() {
        this.time = times;
    }

    @Benchmark
    public void springBeanUtilTest() {
        SourceVO sourceVO = getSourceVO();
        for (int i = 0; i < time; i++) {
            TargetVO targetVO = new TargetVO();
            BeanUtils.copyProperties(sourceVO, targetVO);
        }
    }

    @Benchmark
    public void apacheBeanUtilTest() throws Exception {
        SourceVO sourceVO = getSourceVO();
        for (int i = 0; i < time; i++) {
            TargetVO targetVO = new TargetVO();
            org.apache.commons.beanutils.BeanUtils.copyProperties(targetVO, sourceVO);
        }

    }

    @Benchmark
    public void beanCopierTest() {
        SourceVO sourceVO = getSourceVO();
        for (int i = 0; i < time; i++) {
            TargetVO targetVO = new TargetVO();
            BeanCopier bc = BeanCopier.create(SourceVO.class, TargetVO.class, false);
            bc.copy(sourceVO, targetVO, null);
        }

    }

    @Benchmark
    public void dozerTest() {
        SourceVO sourceVO = getSourceVO();
        for (int i = 0; i < time; i++) {
            TargetVO map = mapper.map(sourceVO, TargetVO.class);
        }
    }

    @Benchmark
    public void orikaTest() {
        SourceVO sourceVO = getSourceVO();
        for (int i = 0; i < time; i++) {
            MapperFacade mapper = mapperFactory.getMapperFacade();
            TargetVO map = mapper.map(sourceVO, TargetVO.class);
        }
    }

    private SourceVO getSourceVO() {
        SourceVO sourceVO = new SourceVO();
        sourceVO.setP1(1);
        sourceVO.setP2(2L);
        sourceVO.setP3(new Integer(3).byteValue());
        sourceVO.setDate1(new Date());
        sourceVO.setPattr1("1");
        sourceVO.setIn(new SourceVO.Inner(1));
        sourceVO.setFullName("alben");
        return sourceVO;
    }

}