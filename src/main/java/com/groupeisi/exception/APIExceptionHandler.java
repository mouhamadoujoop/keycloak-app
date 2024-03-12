package com.groupeisi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class APIExceptionHandler {

    /**
     * Cette methode définit un gestionnaire d'exceptions spécifique pour les exceptions de type RequestException.
     * Lorsqu'une telle exception est levée, cette méthode est appelée pour créer une réponse appropriée
     * qui inclut les informations de l'exception dans le corps de la réponse.
     **/
    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<APIException> handleRequestException(RequestException e) {
        APIException exception = new APIException(e.getMessage(), e.getStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exception, e.getStatus());
    }

    /**
     * Cette méthode est utilisée pour gérer l'exception EntityNotFoundException. Lorsqu'une telle exception est levée,
     * cette méthode crée une instance de APIException avec un message d'erreur, un statut HTTP et une heure actuelle,
     * puis renvoie cette instance encapsulée dans une réponse HTTP avec le statut 404 - Non trouvé.
     **/
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<APIException> handleEntityNotFoundException(EntityNotFoundException e) {
        APIException exception = new APIException(e.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    /**
     * Lorsqu'une NumberFormatException est levée dans une autre méthode du même contrôleur, cette méthode sera appelée
     * pour créer et renvoyer une réponse HTTP avec un code de statut "BAD_REQUEST" (400) et une instance de l'objet APIException.
     **/
    @ExceptionHandler(value = {NumberFormatException.class})
    public ResponseEntity<APIException> handleNumberFormatException(NumberFormatException e) {
        APIException exception = new APIException(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Cette methode capture les exceptions de type MethodArgumentNotValidException, crée un objet APIException pour encapsuler
     * les détails de l'exception, puis renvoie une réponse HTTP avec cet objet et un code de statut "Bad Request" (400)
     * en tant que réponse à la requête.
     **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder("Validation des données échouée : ");
        // Parcourir les erreurs de validation pour chaque champ
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessage
                .append(error.getDefaultMessage());
        }
        // Créer l'objet APIException avec le message d'erreur personnalisé
        APIException exception = new APIException(errorMessage.toString(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}