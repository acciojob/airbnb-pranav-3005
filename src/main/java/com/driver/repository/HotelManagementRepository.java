package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class HotelManagementRepository {
    HashMap<String, Hotel> hotelMap=new HashMap<>(); //(hotel name, hotel)
    HashMap<Integer, User> userMap=new HashMap<>(); //(aadhar,user)
    HashMap<String, Booking> bookingMap=new HashMap<>(); //(book id, book)
    HashMap<Integer, ArrayList<Booking> > userBooking=new HashMap<>(); // (user's aadhar, user's bookings)

    public HashMap<String, Hotel> gethotelMap() {
        return hotelMap;
    }
    public HashMap<Integer, User> getuserMap(){
        return userMap;
    }
    public HashMap<String, Booking> getbookingMap()
    {
        return bookingMap;
    }
    public HashMap<Integer, ArrayList<Booking> > getuserBooking()
    {
        return userBooking;
    }

    public void addHotel(Hotel hotel) {
        hotelMap.put(hotel.getHotelName(),hotel);
    }

    public void addUser(int i, User user) {
        userMap.put(i,user);
    }

    public void bookARoom(String bookingId, Booking booking) {

        bookingMap.put(bookingId,booking);

        //also add to user's booking map

        int aadhar=bookingMap.get(bookingId).getBookingAadharCard();
        if(!userBooking.containsKey(aadhar) )
        {
            userBooking.put( aadhar , new ArrayList<>() );
        }

        userBooking.get(aadhar).add(booking);
    }
}