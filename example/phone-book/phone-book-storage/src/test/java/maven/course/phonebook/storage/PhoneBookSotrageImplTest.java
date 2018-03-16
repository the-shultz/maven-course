package maven.course.phonebook.storage;

import maven.course.phonebook.common.PhoneBookEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PhoneBookSotrageImplTest {

    private PhoneBookStorage phoneBookStorage;

    @Before
    public void before() {
        phoneBookStorage = new PhoneBookStorageImpl();
    }

    @Test
    public void testAddNewEntry() {

        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("mosihe", "ufnik", "0011223344"));
        Assert.assertTrue("Contact is not inserted", 1 == phoneBookStorage.getPhonesList().size());

        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        Assert.assertTrue("Contact is not inserted", 2 == phoneBookStorage.getPhonesList().size());

        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        Assert.assertTrue("Existing contact is inserted", 2 == phoneBookStorage.getPhonesList().size());

    }

    @Test
    public void testGetPhonesList() {
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("moishe", "ufnik", "0011223344"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("kipi", "kipod", "88372949"));

        List<PhoneBookEntry> phonesList = phoneBookStorage.getPhonesList();

        Assert.assertTrue("Not all contacts are inserted", 3 == phonesList.size());
        Assert.assertEquals("moishe",phonesList.get(0).getFirstName());
        Assert.assertEquals("bentz",phonesList.get(1).getLastName());
        Assert.assertEquals("88372949",phonesList.get(2).getPhone());
    }

    @Test
    public void testRemovePhoneBookEntry() {
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("moishe", "ufnik", "0011223344"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("kipi", "kipod", "88372949"));

        Assert.assertTrue("Not all contacts are inserted", 3 == phoneBookStorage.getPhonesList().size());

        phoneBookStorage.removePhoneBookEntry(new PhoneBookEntry("a","b", "9988776655"));
        Assert.assertTrue("Failed to remove entry", 2 == phoneBookStorage.getPhonesList().size());
        Assert.assertTrue("Removed wrong entry", "moishe".equals(phoneBookStorage.getPhonesList().get(0).getFirstName()));
        Assert.assertTrue("Failed to remove entry", "kipi".equals(phoneBookStorage.getPhonesList().get(1).getFirstName()));

        phoneBookStorage.removePhoneBookEntry(new PhoneBookEntry("a","b", "111"));
        Assert.assertTrue("Oops removed unnecessary entry", 2 == phoneBookStorage.getPhonesList().size());

    }

    @Test
    public void testFindEntryByPhone() {
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("moishe", "ufnik", "0011223344"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("kipi", "kipod", "88372949"));

        PhoneBookEntry entryByPhone = phoneBookStorage.findEntryByPhone("88372949");
        Assert.assertNotNull(entryByPhone);
        Assert.assertEquals("kipi", entryByPhone.getFirstName());
        Assert.assertEquals("kipod", entryByPhone.getLastName());

        entryByPhone = phoneBookStorage.findEntryByPhone("111");
        Assert.assertNull(entryByPhone);

    }

    @Test
    public void testFindEntryByFirstName() {
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("moishe", "ufnik", "0011223344"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "bentz", "9988776655"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "kipod", "88372949"));

        List<PhoneBookEntry> entriesByFirstName = phoneBookStorage.findEntryByFirstName("arik");
        Assert.assertTrue(2 == entriesByFirstName.size());
        Assert.assertEquals("bentz", entriesByFirstName.get(0).getLastName());
        Assert.assertEquals("kipod", entriesByFirstName.get(1).getLastName());

        entriesByFirstName = phoneBookStorage.findEntryByFirstName("aaa");
        Assert.assertNotNull(entriesByFirstName);
        Assert.assertTrue(0 == entriesByFirstName.size());
    }

    @Test
    public void testFindEntryByLastName() {
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("moishe", "ufnik", "0011223344"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("arik", "ufnik", "9988776655"));
        phoneBookStorage.addPhoneBookEntry(new PhoneBookEntry("kipi", "kipod", "88372949"));

        List<PhoneBookEntry> entriesByLastName = phoneBookStorage.findEntryByLastName("ufnik");
        Assert.assertTrue(2 == entriesByLastName.size());
        Assert.assertEquals("moishe", entriesByLastName.get(0).getFirstName());
        Assert.assertEquals("arik", entriesByLastName.get(1).getFirstName());

        entriesByLastName = phoneBookStorage.findEntryByLastName("aaa");
        Assert.assertNotNull(entriesByLastName);
        Assert.assertTrue(0 == entriesByLastName.size());

    }
}
