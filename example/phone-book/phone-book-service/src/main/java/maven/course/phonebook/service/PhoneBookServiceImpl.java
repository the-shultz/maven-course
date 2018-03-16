package maven.course.phonebook.service;

import maven.course.phonebook.common.PhoneBookEntry;
import maven.course.phonebook.storage.PhoneBookStorage;
import maven.course.phonebook.storage.PhoneBookStorageImpl;

import java.util.List;

public class PhoneBookServiceImpl implements PhoneBookService {

    private PhoneBookStorage phoneBookStorage;

    public PhoneBookServiceImpl() {
        phoneBookStorage = new PhoneBookStorageImpl();
    }

    @Override
    public boolean addContact(String firstName, String lastName, String phone) {

        if (phoneBookStorage.findEntryByPhone(phone) == null) {
            PhoneBookEntry entry = new PhoneBookEntry(firstName, lastName, phone);
            phoneBookStorage.addPhoneBookEntry(entry);
            return true;
        }

        return false;
    }

    @Override
    public PhoneBookEntry getContact(String firstName) {
        List<PhoneBookEntry> entryByFirstName = phoneBookStorage.findEntryByFirstName(firstName);
        if (entryByFirstName.size() > 0) {
            return entryByFirstName.get(0);
        }
        return null;
    }

    @Override
    public boolean removeContactByPhone(String phone) {
        if (phoneBookStorage.findEntryByPhone(phone) != null) {
            PhoneBookEntry entry = new PhoneBookEntry(null, null, phone);
            phoneBookStorage.removePhoneBookEntry(entry);
            return true;
        }
        return false;
    }

    @Override
    public int getTotalContacts() {
        return phoneBookStorage.getPhonesList().size();
    }
}
