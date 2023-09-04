package com.driver.service;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repository.HotelManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class HotelManagementService {
    @Autowired
    HotelManagementRepository repo=new HotelManagementRepository();

    public String addHotel(Hotel hotel) {
        if(hotel==null || hotel.getHotelName().equals(null) || repo.gethotelMap().containsKey(hotel.getHotelName()) )
            return "FAILURE";

        repo.addHotel(hotel);
        return "SUCCESS";

    }

    public Integer addUser(User user) {
        repo.addUser(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public int bookARoom(Booking booking) {
        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

        HashMap<String,Hotel> hotelmap= repo.gethotelMap();
        HashMap<String,Booking> bookingMap= repo.getbookingMap();

        if( booking.getNoOfRooms() > hotelmap.get(booking.getHotelName()).getAvailableRooms() )
        {
            return -1;
        }

        booking.setBookingId( String.valueOf(UUID.randomUUID()) );
        int amount= booking.getNoOfRooms() *  hotelmap.get(booking.getHotelName()).getPricePerNight();
        booking.setAmountToBePaid(amount);

        repo.bookARoom(booking.getBookingId(), booking);

        return amount;


    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible

        List<Facility> hfacility= repo.gethotelMap().get(hotelName).getFacilities(); //hotel facility

        for(Facility i :newFacilities)
        {
            if(!hfacility.contains(i))
            {
                repo.gethotelMap().get(hotelName).getFacilities().add(i);
            }
        }

        return repo.gethotelMap().get(hotelName);
    }

    public String getHotelWithMostFacilities() {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        HashMap<String,Hotel> hotelMap= repo.gethotelMap();

        int maxF=0;
        String maxFH=""; //max facility hotel

        for(String i : hotelMap.keySet())
        {
            int curF=hotelMap.get(i).getFacilities().size();
            if(curF>maxF)
            {
                maxF=curF;
                maxFH=i;
            }
            else if(curF==maxF)
            {
                if(maxFH.compareTo(i)>0)
                {
                    maxFH=i;
                }
            }
        }

        if(maxF==0)
            return "";

        return maxFH;

    }

    public int getBookings(Integer aadharCard) {
        HashMap<Integer, ArrayList<Booking> > userBooking = repo.getuserBooking();

        if(!userBooking.containsKey(aadharCard))
        {
            return 0;
        }

        return userBooking.get(aadharCard).size();
    }
}