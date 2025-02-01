package ca.bc.gov.restapi.results.oracle.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaginationUtilTest {

  @Test
  @DisplayName("Get lastPage test")
  void getLastPageTest() {
    Assertions.assertEquals(10, PaginationUtil.getLastPage(98, 10));
    Assertions.assertEquals(10, PaginationUtil.getLastPage(99, 10));
    Assertions.assertEquals(10, PaginationUtil.getLastPage(100, 10));
    Assertions.assertEquals(11, PaginationUtil.getLastPage(101, 10));

    Assertions.assertEquals(1, PaginationUtil.getLastPage(3, 5));
    Assertions.assertEquals(1, PaginationUtil.getLastPage(4, 5));
    Assertions.assertEquals(1, PaginationUtil.getLastPage(5, 5));
    Assertions.assertEquals(2, PaginationUtil.getLastPage(6, 5));

    Assertions.assertEquals(0, PaginationUtil.getLastPage(0, 5));
    Assertions.assertEquals(0, PaginationUtil.getLastPage(5, 0));
  }

  @Test
  @DisplayName("Get startIndex test")
  void getStartIndexTest() {
    Assertions.assertEquals(30, PaginationUtil.getStartIndex(3, 10));
    Assertions.assertEquals(10, PaginationUtil.getStartIndex(1, 10));
    Assertions.assertEquals(5, PaginationUtil.getStartIndex(1, 5));
    Assertions.assertEquals(0, PaginationUtil.getStartIndex(0, 5));
    Assertions.assertEquals(150, PaginationUtil.getStartIndex(10, 15));
  }

  @Test
  @DisplayName("End index test")
  void getEndIndexTest() {
    Assertions.assertEquals(40, PaginationUtil.getEndIndex(30, 10, 47));
    Assertions.assertEquals(47, PaginationUtil.getEndIndex(40, 10, 47));
    Assertions.assertEquals(45, PaginationUtil.getEndIndex(40, 5, 100));
    Assertions.assertEquals(50, PaginationUtil.getEndIndex(45, 5, 100));
    Assertions.assertEquals(40, PaginationUtil.getEndIndex(45, 5, 40));
  }
}
