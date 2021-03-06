package com.lihe.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by leo on 9/6/15.
 */
@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice {

    protected Logger logger;

    public GlobalExceptionHandlingControllerAdvice() {
        logger = LoggerFactory.getLogger(getClass());
    }

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . . . */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Convert a predefined exception to an HTTP Status code
     */
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    // 409
    @ExceptionHandler(DataIntegrityViolationException.class) public void conflict(
        DataIntegrityViolationException exception) {
        logger.error("Request raised a DataIntegrityViolationException", exception);
        // Nothing to do
    }

    /**
     * Convert a predefined exception to an HTTP Status code and specify the
     * name of a specific view that will be used to display the error.
     *
     * @return Exception view.
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class}) public String databaseError(
        Exception exception) {
        // Nothing to do. Return value 'databaseError' used as logical view name
        // of an error page, passed to view-resolver(s) in usual way.
        logger.error("Request raised " + exception.getClass().getSimpleName(), exception);
        return "databaseError";
    }

    /**
     * Demonstrates how to take total control - setup a model, add useful
     * information and return the "support" view name. This method explicitly
     * creates and returns
     *
     * @param req       Current HTTP request.
     * @param exception The exception thrown - always .
     * @return The model and view used by the DispatcherServlet to generate
     * output.
     * @throws Exception
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class}) public Msg handleError(
        HttpServletRequest req, Exception exception) throws Exception {
        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null)
            throw exception;
        logger.error("Request: " + req.getRequestURI() + " error ", exception);
        return new Msg(500, exception.getMessage());
    }
}
