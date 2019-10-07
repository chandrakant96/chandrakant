package com.whr.user.pojo;

public class AppoinmentBookingPojo {

    public static class TimeList {

        String strtime;

        public String getStrtime() {
            return strtime;
        }

        public void setStrtime(String strtime) {
            this.strtime = strtime;
        }

        @Override
        public String toString() {
            return "TimeList{" +
                    "strtime='" + strtime + '\'' +
                    '}';
        }
    }


    public static class BookedDates {
        String BookedDate;

        public String getBookedDate() {
            return BookedDate;
        }

        public void setBookedDate(String bookedDate) {
            BookedDate = bookedDate;
        }

        @Override
        public String toString() {
            return "BookedDates{" +
                    "BookedDate='" + BookedDate + '\'' +
                    '}';
        }
    }


}





















/*
package com.whr.user.pojo;

import java.util.List;

*/
/**
 * Created by lenovo on 4/11/2017.
 *//*


public class AppoinmentBookingPojo
{

    List<TimeList> timeList;
    List<BookedDates> bookedDatesList;

    public List<BookedDates> getBookedDatesList() {
        return bookedDatesList;
    }

    public void setBookedDatesList(List<BookedDates> bookedDatesList) {
        this.bookedDatesList = bookedDatesList;
    }



    public List<TimeList> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeList> timeList) {
        this.timeList = timeList;
    }

    public static class  TimeList
    {

        public String getStrtime() {
            return strtime;
        }

        public void setStrtime(String strtime) {
            this.strtime = strtime;
        }

        String strtime;
    }





    public static class BookedDates
    {
        String BookedDate;

        public BookedDates() {
        }

        public String getBookedDate() {
            return BookedDate;
        }

        public void setBookedDate(String bookedDate) {
            BookedDate = bookedDate;
        }
    }



}
*/
