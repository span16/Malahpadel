package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Mon Application JavaFX";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_1082871571947-fb2vbccg03knjsjnr0t2evrfr78d5gm1.apps.googleusercontent.com.json";

    public static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(
                flow, new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver())
                .authorize("user");
    }

    // Récupère les 5 prochains événements (méthode existante)
    public static List<Event> getNextFiveEvents() throws IOException, GeneralSecurityException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Events events = service.events().list("primary")
                .setMaxResults(5)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return events.getItems();
    }

    // Récupère TOUS les événements de votre calendrier (avec pagination)
    public static List<Event> getAllEvents() throws IOException, GeneralSecurityException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Récupère jusqu'à 2500 événements maximum (la limite par défaut de l'API)
        Events events = service.events().list("primary")
                .setSingleEvents(true)
                .setMaxResults(2500)
                .execute();
        List<Event> allEvents = events.getItems();

        // Pagination : si nextPageToken est présent, on récupère les pages suivantes
        String pageToken = events.getNextPageToken();
        while (pageToken != null) {
            events = service.events().list("primary")
                    .setSingleEvents(true)
                    .setMaxResults(2500)
                    .setPageToken(pageToken)
                    .execute();
            allEvents.addAll(events.getItems());
            pageToken = events.getNextPageToken();
        }

        return allEvents;
    }

    // Méthode d'ajout d'un événement dans Google Calendar ; retourne l'ID de l'événement créé
    public static String addEventToGoogleCalendar(models.EmploiDuTemps edt) throws IOException, GeneralSecurityException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Event event = new Event();
        event.setSummary("Match: " + edt.getEquipe1().getNomEquipe() + " vs " + edt.getEquipe2().getNomEquipe());
        long startMillis = edt.getDate().getTime();
        long endMillis = startMillis + (2 * 60 * 60 * 1000); // 2 heures de match
        event.setStart(new EventDateTime()
                .setDateTime(new DateTime(startMillis))
                .setTimeZone("Europe/Paris"));
        event.setEnd(new EventDateTime()
                .setDateTime(new DateTime(endMillis))
                .setTimeZone("Europe/Paris"));
        Event createdEvent = service.events().insert("primary", event).execute();
        return createdEvent.getId();
    }

    // Met à jour un événement existant dans Google Calendar
    public static void updateGoogleCalendarEvent(String googleEventId, models.EmploiDuTemps edt)
            throws IOException, GeneralSecurityException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Récupère l'événement existant
        Event event = service.events().get("primary", googleEventId).execute();
        // Met à jour le résumé
        event.setSummary("Match: " + edt.getEquipe1().getNomEquipe() + " vs " + edt.getEquipe2().getNomEquipe());
        // Met à jour les dates
        long startMillis = edt.getDate().getTime();
        long endMillis = startMillis + (2 * 60 * 60 * 1000);
        event.setStart(new EventDateTime()
                .setDateTime(new DateTime(startMillis))
                .setTimeZone("Europe/Paris"));
        event.setEnd(new EventDateTime()
                .setDateTime(new DateTime(endMillis))
                .setTimeZone("Europe/Paris"));
        // Envoie la mise à jour
        service.events().update("primary", googleEventId, event).execute();
    }
}
