package maven.course.phonebook.usage;

import maven.course.phonebook.common.PhoneBookEntry;
import maven.course.phonebook.service.PhoneBookService;
import maven.course.phonebook.service.PhoneBookServiceImpl;

public class Main {

    public static void main(String[] args) {
        PhoneBookService phoneBook = new PhoneBookServiceImpl();
        phoneBook.addContact("moishe","ufnik", "111222333");
        phoneBook.addContact("arik","bentz", "444555666");
        phoneBook.addContact("kipi","kipod", "777888999");

        PhoneBookEntry kipi = phoneBook.getContact("kipi");
        System.out.println(kipi);
    }
}
