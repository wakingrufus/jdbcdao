package com.github.wakingrufus.jdbcdao;

import org.junit.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 */
public class JdbcDaoTest {

    public JdbcDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of findOne method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testFindOne() {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(Calendar.getInstance().getTime());
        tc1.setString1("tc1");
        dao.create(tc1);
        TestClass1 tc2 = dao.findOne(tc1.getId());
        compareClass1(tc1, tc2);
    }

    private void compareClass1(TestClass1 expected, TestClass1 actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate1(), actual.getDate1());
        assertEquals(0, expected.getDecimal1().compareTo(actual.getDecimal1()));
        assertEquals(expected.getString1(), actual.getString1());
    }

    /**
     * Test of update method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testUpdate() {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(Calendar.getInstance().getTime());
        tc1.setString1("tc1");
        dao.create(tc1);
        tc1.setString1("testChange");
        dao.update(tc1);
        assertEquals("testChange", dao.findOne(tc1.getId()).getString1());

        JdbcDao<TestClass2, Long> dao2 = new JdbcDao<>(MockDatasource.getInstance(), TestClass2.class);
        TestClass2 tc2 = new TestClass2();
        dao2.create(tc2);
        BigDecimal bdValue = new BigDecimal("1.01");
        tc2.setBigDecimal(bdValue);
        dao2.update(tc2);
        assertEquals(0, bdValue.compareTo(dao2.findOne(tc2.getId()).getBigDecimal()));

    }

    /**
     * Test of create method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testCreate() {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        List<TestClass1> findAll = dao.findAll();
        int lastKey = findAll.get(findAll.size() - 1).getId();
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(Calendar.getInstance().getTime());
        tc1.setString1("tc1");
        dao.create(tc1);
        assertEquals(lastKey + 1, tc1.getId());

        JdbcDao<TestClass2, Long> dao2 = new JdbcDao<>(MockDatasource.getInstance(), TestClass2.class);
        TestClass2 tc2 = new TestClass2();
        BigDecimal bdValue = new BigDecimal("1.01");
        tc2.setBigDecimal(bdValue);
        dao2.create(tc2);
        assertEquals(0, bdValue.compareTo(dao2.findOne(tc2.getId()).getBigDecimal()));
    }

    /**
     * Test of findAll method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testFindAll() {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        int startingSize = dao.findAll().size();
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(Calendar.getInstance().getTime());
        tc1.setString1("tc1");
        dao.create(tc1);
        TestClass1 tc2 = new TestClass1();
        tc2.setBoolean1(true);
        tc2.setDecimal1(BigDecimal.ZERO);
        tc2.setDate1(Calendar.getInstance().getTime());
        tc2.setString1("tc2");
        dao.create(tc2);
        assertEquals(startingSize + 2, dao.findAll().size());
    }

    /**
     * Test of convertFieldToColumnName method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testConvertFieldToColumnName() throws NoSuchFieldException {
        JdbcDao<TestClass2, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass2.class);
        assertEquals("class2Id", dao.convertFieldToColumnName(TestClass2.class.getDeclaredField("id")));
        assertEquals("stringData", dao.convertFieldToColumnName(TestClass2.class.getDeclaredField("string2")));
    }

    /**
     * Test of findWhereCriteria method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testFindWhereCriteria() throws NoSuchFieldException {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(Calendar.getInstance().getTime());
        tc1.setString1("tc1");
        dao.create(tc1);
        TestClass1 tc2 = new TestClass1();
        tc2.setBoolean1(false);
        tc2.setDecimal1(BigDecimal.ZERO);
        tc2.setDate1(Calendar.getInstance().getTime());
        tc2.setString1("tc2");
        dao.create(tc2);
        TestClass1 tc3 = new TestClass1();
        tc3.setBoolean1(false);
        tc3.setDecimal1(BigDecimal.ZERO);
        tc3.setDate1(Calendar.getInstance().getTime());
        tc3.setString1("tc3");
        dao.create(tc3);

        Field booleanField = TestClass1.class.getDeclaredField("boolean1");
        Field stringField = TestClass1.class.getDeclaredField("string1");

        Criteria trueCriteria = new ComparisonCriteria(booleanField, ComparisonOperator.EQUALS, Boolean.TRUE);
        Criteria falseCriteria = new ComparisonCriteria(booleanField, ComparisonOperator.EQUALS, Boolean.FALSE);
        Criteria tc1Critera = new ComparisonCriteria(stringField, ComparisonOperator.EQUALS, "tc1");
        Criteria tc2Critera = new ComparisonCriteria(stringField, ComparisonOperator.EQUALS, "tc2");
        Criteria tc3Critera = new ComparisonCriteria(stringField, ComparisonOperator.EQUALS, "tc3");

        Criteria compoundCriteria1 = new CompoundCriteria(trueCriteria, CompoundOperator.OR, tc1Critera);
        assertEquals(1, dao.findWhereCriteria(compoundCriteria1).size());

        Criteria compoundCriteria2 = new CompoundCriteria(falseCriteria, CompoundOperator.OR, tc2Critera);
        assertEquals(2, dao.findWhereCriteria(compoundCriteria2).size());

        Criteria compoundCriteria3 = new CompoundCriteria(falseCriteria, CompoundOperator.AND, tc1Critera);
        assertEquals(0, dao.findWhereCriteria(compoundCriteria3).size());

        Criteria compoundCriteria4 = new CompoundCriteria(falseCriteria, CompoundOperator.OR, tc1Critera);
        assertEquals(3, dao.findWhereCriteria(compoundCriteria4).size());

    }

    @Test
    public void testFindWhereDateRangeCriteria() throws NoSuchFieldException {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 5 - 1, 5);

        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        TestClass1 tc1 = new TestClass1();
        tc1.setBoolean1(true);
        tc1.setDecimal1(BigDecimal.ZERO);
        tc1.setDate1(calendar.getTime());

        tc1.setString1("tc1");
        dao.create(tc1);

        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date endDate = calendar.getTime();

        Field dateField = TestClass1.class.getDeclaredField("date1");

        Criteria startDateCriteria = new ComparisonCriteria(dateField, ComparisonOperator.GREATER_THAN, startDate);
        Criteria endDateCriteria = new ComparisonCriteria(dateField, ComparisonOperator.LESS_THAN, endDate);
        Criteria combinedCriteria = new CompoundCriteria(startDateCriteria, CompoundOperator.AND, endDateCriteria);

        assertEquals(1, dao.findWhereCriteria(combinedCriteria).size());

    }

    /**
     * Test of delete method, of class com.github.wakingrufus.jdbcdao.JdbcDao.
     */
    @Test
    public void testDelete() {
        JdbcDao<TestClass1, Integer> dao = new JdbcDao<>(MockDatasource.getInstance(), TestClass1.class);
        TestClass1 tc2 = new TestClass1();
        tc2.setBoolean1(true);
        tc2.setDecimal1(BigDecimal.ZERO);
        tc2.setDate1(Calendar.getInstance().getTime());
        tc2.setString1("tc2");
        dao.create(tc2);
        dao.delete(tc2);
        assertNull(dao.findOne(tc2.getId()));
    }

}
