package com.csy.druid.demo.protobuf;

import com.google.protobuf.ByteString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p>Description：</p>
 *
 * @author shuyun.cheng
 * @date 2020/8/7 20:50
 */
public class ProtoTest {
    public static void main(String[] args) {
        try {
            /* Step1：生成 personTest 对象 */
            // personTest 构造器
            PersonTestProtobuf.PersonTest.Builder personBuilder = PersonTestProtobuf.PersonTest.newBuilder();
            // personTest 赋值
            personBuilder.setName("Jet Chen");
            personBuilder.setEmail("ckk505214992@ .com");
            personBuilder.setSex(PersonTestProtobuf.PersonTest.Sex.MALE);

            // 内部的 PhoneNumber 构造器
            PersonTestProtobuf.PersonTest.PhoneNumber.Builder phoneNumberBuilder
                    = PersonTestProtobuf.PersonTest.PhoneNumber.newBuilder();
            // PhoneNumber 赋值
            phoneNumberBuilder.setType(PersonTestProtobuf.PersonTest.PhoneNumber.PhoneType.MOBILE);
            phoneNumberBuilder.setNumber("17717037257");

            // personTest 设置 PhoneNumber
            personBuilder.addPhone(phoneNumberBuilder);

            // 生成 personTest 对象
            PersonTestProtobuf.PersonTest personTest = personBuilder.build();

            /* Step2：序列化和反序列化 */
            // 方式一 byte[]：
            // 序列化
            byte[] bytes = personTest.toByteArray();
            // 反序列化
            PersonTestProtobuf.PersonTest personTestResult = PersonTestProtobuf.PersonTest.parseFrom(bytes);
            System.out.println(personTestResult);
            System.out.println(String.format("反序列化得到的信息，姓名：%s，性别：%d，手机号：%s", personTestResult.getName(), personTest.getSexValue(), personTest.getPhone(0).getNumber()));


            // 方式二 ByteString：
            // 序列化
            ByteString byteString = personTest.toByteString();
            System.out.println(byteString.toString());
            // 反序列化
            PersonTestProtobuf.PersonTest personTestResult1 = PersonTestProtobuf.PersonTest.parseFrom(byteString);
            System.out.println(personTestResult1);
            System.out.println(String.format("反序列化得到的信息，姓名：%s，性别：%d，手机号：%s", personTestResult1.getName(), personTest.getSexValue(), personTest.getPhone(0).getNumber()));

            // 方式三 InputStream
            // 粘包,将一个或者多个protobuf 对象字节写入 stream
            // 序列化
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            personTest.writeDelimitedTo(byteArrayOutputStream);
            // 反序列化，从 steam 中读取一个或者多个 protobuf 字节对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            PersonTestProtobuf.PersonTest personTestResult2 = PersonTestProtobuf.PersonTest.parseDelimitedFrom(byteArrayInputStream);
            System.out.println(personTestResult2);
            System.out.println(String.format("反序列化得到的信息，姓名：%s，性别：%d，手机号：%s", personTestResult2.getName(), personTest.getSexValue(), personTest.getPhone(0).getNumber()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
