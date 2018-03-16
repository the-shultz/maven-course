package maven.course.phonebook.service;

import maven.course.phonebook.common.PhoneBookEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PhoneBookServiceImplTest {

    private PhoneBookService phoneBookService;

    @Before
    public void before() {
        phoneBookService = new PhoneBookServiceImpl();
    }

    @Test
    public void testAddContact() {
        boolean result = phoneBookService.addContact("moishe", "ufnik", "1122334455");
        Assert.assertTrue(result);
        Assert.assertTrue(1 == phoneBookService.getTotalContacts());

        result = phoneBookService.addContact("moishe", "ufnik", "1122334455");
        Assert.assertFalse(result);
        Assert.assertTrue(1 == phoneBookService.getTotalContacts());
    }

    @Test
    public void testGetContact() {
        phoneBookService.addContact("moishe", "ufnik", "1122334455");
        phoneBookService.addContact("arik", "bentz", "6677889900");

        PhoneBookEntry moishe = phoneBookService.getContact("moishe");
        Assert.assertEquals("ufnik", moishe.getLastName());
        Assert.assertEquals("1122334455", moishe.getPhone());

        PhoneBookEntry arik = phoneBookService.getContact("arik");
        Assert.assertEquals("bentz", arik.getLastName());
        Assert.assertEquals("6677889900", arik.getPhone());
    }

    @Test
    public void testRemoveContact() {

        phoneBookService.addContact("moishe", "ufnik", "1122334455");
        phoneBookService.addContact("arik", "bentz", "6677889900");
        Assert.assertTrue(2 == phoneBookService.getTotalContacts());

        boolean result = phoneBookService.removeContactByPhone("1122334455");
        Assert.assertTrue(result);
        Assert.assertTrue(1 == phoneBookService.getTotalContacts());

        result = phoneBookService.removeContactByPhone("111");
        Assert.assertFalse(result);
        Assert.assertTrue(1 == phoneBookService.getTotalContacts());

    }

}
