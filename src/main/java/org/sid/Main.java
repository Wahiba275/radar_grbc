package org.sid;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.sid.stubs.RadarGrpc;
import org.sid.stubs.RadarServiceGrpc;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static RadarServiceGrpc.RadarServiceBlockingStub stub;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        stub = RadarServiceGrpc.newBlockingStub(channel);
        Scanner sc = new Scanner(System.in);
        // Create new radar
        System.out.print("Enter radar max speed: ");
        Double maxSpeed = sc.nextDouble();
        System.out.print("Enter radar longitude: ");
        Double longitude = sc.nextDouble();
        System.out.print("Enter radar latitude : ");
        Double latitude = sc.nextDouble();
        String radarId = createRadar(maxSpeed, longitude, latitude);
        System.out.print("Radar created with id: " + radarId);

        // Detect infraction
        int choice = 0;
        while(choice != 2) {
            System.out.println("1. Detect infraction");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle speed: ");
                    Double speed = sc.nextDouble();
                    sc.nextLine(); // Consume the newline character
                    System.out.print("Enter vehicle id: ");
                    String vehicleId = sc.nextLine();
                    System.out.println("Detecting infraction...");
                    processRadar(radarId, vehicleId, speed);
                    break;
                case 2:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static String createRadar(Double maxSpeed, Double longitude, Double latitude) {
        return "RADAR001";
    }
    private static void processRadar(String radarId, String vehicleId, Double speed) {
        RadarGrpc.RadarRequest request = RadarGrpc.RadarRequest.newBuilder()
                .setRadarId(radarId)
                .setVehiculeId(vehicleId)
                .setMaxSpeed(speed)
                .build();
        System.out.println(radarId);
        System.out.println(vehicleId);
        System.out.println(speed);
        RadarGrpc.Infraction infraction = stub.processRadar(request);
        System.out.println("Infraction detected-------------------------------- ");
        System.out.println(infraction);
        System.out.println("--------------------------------------------------- ");
    }
}