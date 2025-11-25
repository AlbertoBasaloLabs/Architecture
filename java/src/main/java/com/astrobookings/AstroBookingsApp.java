package com.astrobookings;

import com.astrobookings.controller.BookingHandler;
import com.astrobookings.controller.FlightHandler;
import com.sun.net.httpserver.HttpServer;
import com.astrobookings.controller.RocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AstroBookingsApp {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/flights", new FlightHandler());
        server.createContext("/bookings", new BookingHandler());
        server.createContext("/rockets", new RocketHandler());

        server.setExecutor(null); // creates a default executor
        server.start();

        System.out.println("Server started on port " + port);
    }
}
