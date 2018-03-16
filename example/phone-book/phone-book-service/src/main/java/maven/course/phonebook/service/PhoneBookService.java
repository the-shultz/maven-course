package maven.course.phonebook.service;

import maven.course.phonebook.common.PhoneBookEntry;

public interface PhoneBookService {

    boolean addContact(String firstName, String lastName, String phone);
    PhoneBookEntry getContact(String firstName);
    boolean removeContactByPhone(String phone);
    int getTotalContacts();
}
