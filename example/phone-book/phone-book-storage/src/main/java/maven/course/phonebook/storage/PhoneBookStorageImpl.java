package maven.course.phonebook.storage;

import maven.course.phonebook.common.PhoneBookEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PhoneBookStorageImpl implements PhoneBookStorage{

    private List<PhoneBookEntry> phones;

    public PhoneBookStorageImpl() {
        phones = new ArrayList<>();
    }

    @Override
    public List<PhoneBookEntry> getPhonesList() {
        return phones;
    }

    @Override
    public void addPhoneBookEntry(PhoneBookEntry phoneBookEntry) {
        if (!containsEntry(phoneBookEntry)) {
            phones.add(phoneBookEntry);
        }
    }

    @Override
    public void removePhoneBookEntry(PhoneBookEntry phoneBookEntry) {
        int indexOfEntry = findEntryIndex(phoneBookEntry);
        if (indexOfEntry != -1) {
            phones.remove(indexOfEntry);
        }
    }

    private int findEntryIndex(PhoneBookEntry phoneBookEntry) {
        int i = 0;
        for (; i < phones.size(); i++) {
            if (phones.get(i).getPhone().equals(phoneBookEntry.getPhone()))
                break;
        }
        if (i >= phones.size()) {
            return -1;
        }
        return i;
    }

    @Override
    public PhoneBookEntry findEntryByPhone(String phoneNumber) {
        List<PhoneBookEntry> matchedEntries = findMatched(entry -> entry.getPhone().equals(phoneNumber));
        return matchedEntries.size() == 0 ? null : matchedEntries.get(0);
    }

    @Override
    public List<PhoneBookEntry> findEntryByFirstName(String firstName) {
        return findMatched(entry -> entry.getFirstName().equals(firstName));
    }

    @Override
    public List<PhoneBookEntry> findEntryByLastName(String lastName) {
        return findMatched(entry -> entry.getLastName().equals(lastName));
    }

    private boolean containsEntry(PhoneBookEntry phoneBookEntry) {
        return phones
                .stream()
                .anyMatch(entry -> entry.getPhone().equals(phoneBookEntry.getPhone()));
    }

    private List<PhoneBookEntry> findMatched(Predicate<PhoneBookEntry> predicate) {
        return phones
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());

    }
}
