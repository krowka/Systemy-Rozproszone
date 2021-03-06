package sr.serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import sr.proto.AddressBookProtos.Person;

public class ProtoSerialization {

    public static void main(String[] args) {
        try {
            new ProtoSerialization().testProto();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testProto() throws IOException {
        Person john =
                Person.newBuilder()
                        .setId(123456)
                        .setName("Włodzimierz Wróblewski")
                        .setEmail("wrobel@poczta.com")
                        .addPhones(
                                Person.PhoneNumber.newBuilder()
                                        .setNumber("+48-12-555-4321")
                                        .setType(Person.PhoneType.HOME))
                        .addPhones(
                                Person.PhoneNumber.newBuilder()
                                        .setNumber("+1")
                                        .setType(Person.PhoneType.MOBILE))
                        .addPhones(
                                Person.PhoneNumber.newBuilder()
                                        .setNumber("+2")
                                        .setType(Person.PhoneType.MOBILE))
						.addPhones(
								Person.PhoneNumber.newBuilder()
										.setNumber("+3")
										.setType(Person.PhoneType.MOBILE))
						.addPhones(
								Person.PhoneNumber.newBuilder()
										.setNumber("+4")
										.setType(Person.PhoneType.MOBILE))
						.addPhones(
								Person.PhoneNumber.newBuilder()
										.setNumber("+5")
										.setType(Person.PhoneType.MOBILE))
						.addPhones(
								Person.PhoneNumber.newBuilder()
										.setNumber("+6")
										.setType(Person.PhoneType.MOBILE))
                        .build();

        byte[] johnser = john.toByteArray(); //TODO

        System.out.println(johnser.length + " " + Arrays.toString(johnser));
        System.out.println(new String(johnser, StandardCharsets.UTF_8));

        long n = 1000;
        System.out.println("Performing proto serialization " + n + " times...");
        for (long i = 0; i < n; i++) {
            johnser = john.toByteArray();
        }
        System.out.println("... finished.");
    }
}
