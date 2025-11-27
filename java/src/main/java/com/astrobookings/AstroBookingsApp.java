package com.astrobookings;

import com.astrobookings.application.AdminHandler;
import com.astrobookings.application.BookingHandler;
import com.astrobookings.application.FlightHandler;
import com.astrobookings.application.RocketHandler;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class AstroBookingsApp {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/flights", new FlightHandler());
        server.createContext("/bookings", new BookingHandler());
        server.createContext("/rockets", new RocketHandler());
        server.createContext("/admin/cancel-flights", new AdminHandler());

        server.setExecutor(null); // creates a default executor
        server.start();

        System.out.println("AstroBookings started on port " + port);
    }
}
