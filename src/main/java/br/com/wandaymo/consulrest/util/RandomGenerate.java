package br.com.wandaymo.consulrest.util;

import br.com.wandaymo.consulrest.entity.Person;
import br.com.wandaymo.consulrest.entity.Restrictive;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
public class RandomGenerate {

    private static final List<String> NAMES = List.of("Jorge", "Miguel", "Isabela", "Victor", "Benedito", "Analu", "Sara",
            "Vicente", "Rebeca", "Flávia", "Alberto", "Benjamin", "Daniel", "Sebastião", "Larissa", "Andreia");

    public Person generatePerson() {
        var name = randomName();
        var person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setName(name);
        person.setBornDate(generateRandomLocalDate());
        person.setPersonType("F");
        person.setCpfCnpj(randomCpf());
        person.setRegisterNumber(randomRgCep(true));
        person.setCreatedDate(LocalDate.now());
        person.setEmail(name.toLowerCase() + "@email.com");
        person.setCep(randomRgCep(false));
        person.setAddress("Rua " + randomName() + " " + randomName() + " " + randomName());
        person.setAddressNumber(String.valueOf(ThreadLocalRandom.current().nextInt(100, 9999)));
        person.setDistrict(randomString(2, false));
        person.setCity(randomString(1, false) +
                randomString(ThreadLocalRandom.current().nextInt(4, 20), true));
        person.setState(randomString(1, false) +
                randomString(ThreadLocalRandom.current().nextInt(4, 15), true));
        person.setPhoneNumber(ThreadLocalRandom.current().nextInt(10, 99) + "9" +
                ThreadLocalRandom.current().nextInt(10000000, 99999999));
        return person;
    }

    public Restrictive generateRestrictive() {
        var restrictive = new Restrictive();
        restrictive.setId(UUID.randomUUID().toString());
        restrictive.setType(randomString(3, false));
        restrictive.setDescription(randomString(10, true));
        restrictive.setMaximumValue(ThreadLocalRandom.current().nextDouble());
        restrictive.setMinimumValue(ThreadLocalRandom.current().nextDouble());

        return restrictive;
    }

    private LocalDate generateRandomLocalDate() {
        long minDay = LocalDate.of(2010, Month.JANUARY, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = minDay + ThreadLocalRandom.current().nextLong(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private String randomName() {
        return NAMES.get(ThreadLocalRandom.current().nextInt(NAMES.size()));
    }

    private int randomize(int n) {
        return (int) (Math.random() * n);
    }

    private int mod(int dividendo) {
        int divisor = 11;
        return (int) Math.round(dividendo - (Math.floor(dividendo / divisor) * divisor));
    }

    private String randomCpf() {
        int n = 9;
        int n1 = randomize(n);
        int n2 = randomize(n);
        int n3 = randomize(n);
        int n4 = randomize(n);
        int n5 = randomize(n);
        int n6 = randomize(n);
        int n7 = randomize(n);
        int n8 = randomize(n);
        int n9 = randomize(n);
        int d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;

        d1 = 11 - (mod(d1));

        if (d1 >= 10) {
            d1 = 0;
        }
        int d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;
        d2 = 11 - (mod(d2));

        if (d2 >= 10) {
            d2 = 0;
        }
        return Strings.EMPTY + n1 + n2 + n3 + '.' + n4 + n5 + n6 + '.' + n7 + n8 + n9 + '-' + d1 + d2;
    }

    private String randomRgCep(boolean rg) {
        SecureRandom randomNumber = new SecureRandom();
        int n1 = randomNumber.nextInt(10);
        int n2 = randomNumber.nextInt(10);
        int n3 = randomNumber.nextInt(10);
        int n4 = randomNumber.nextInt(10);
        int n5 = randomNumber.nextInt(10);
        int n6 = randomNumber.nextInt(10);
        int n7 = randomNumber.nextInt(10);
        int n8 = randomNumber.nextInt(10);
        int n9 = randomNumber.nextInt(10);

        if (rg) {
            return Strings.EMPTY + n1 + n2 + "." + n3 + n4 + n5 + "." + n6 + n7 + n8 + "-" + n9;
        } else {
            return Strings.EMPTY + n1 + n2 + n3 + n4 + n5 + "-" + n6 + n7 + n8;
        }
    }

    private String randomString(int len, boolean lowerCase) {
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);

        if (lowerCase) {
            string = "abcdefghijklmnopqrstuvwxyz";
        }

        for (int i = 0; i < len; i++) {
            sb.append(string.charAt(rnd.nextInt(string.length())));
        }
        return sb.toString();
    }
}
