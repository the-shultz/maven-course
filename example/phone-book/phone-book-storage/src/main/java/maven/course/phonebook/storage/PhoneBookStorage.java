package maven.course.phonebook.storage;

import maven.course.phonebook.common.PhoneBookEntry;

import java.util.List;

public interface PhoneBookStorage {

    // CRUD
    List<PhoneBookEntry> getPhonesList();
    void addPhoneBookEntry(PhoneBookEntry phoneBookEntry);
    void removePhoneBookEntry(PhoneBookEntry phoneBookEntry);

    // FIND
    PhoneBookEntry findEntryByPhone(String phoneNumber);
    List<PhoneBookEntry> findEntryByFirstName(String firstName);
    List<PhoneBookEntry> findEntryByLastName(String lastName);
}
