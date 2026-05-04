package cg.hospital.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.NoSuchElementException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// ── Utility: builds the standard error body ───────────────────────────────
	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String error, String message,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp details", LocalDateTime.now().toString());
		body.put("status code", status.value());
		body.put("error caused due to", error);
		body.put("message", message);
		body.put("path", request.getDescription(false).replace("uri=", ""));
		return new ResponseEntity<>(body, status);
	}

	// ── 404: resource not found ───────────────────────────────────────────────
	@ExceptionHandler(org.springframework.data.rest.webmvc.ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFound(
			org.springframework.data.rest.webmvc.ResourceNotFoundException ex, WebRequest request) {
		return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
	}

	// ── 400: bad / malformed request ──────────────────────────────────────────
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex, WebRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
	}

	// ── 400: illegal argument (e.g. composite id format wrong like "got: 2") ─
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
//		return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
//	}

	// ── 500: JPA/Hibernate entity not found during lazy load ─────────────────
	@ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex,
			WebRequest request) {
		return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
	}

	// ── 400: wrong type in path/query param (e.g. "abc" where int expected) ──
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		String msg = String.format("Parameter '%s' should be of type %s", ex.getName(),
				ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
		return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, request);
	}

	// ── 409: DB constraint violation (duplicate PK, FK violation, etc.) ───────
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex,
			WebRequest request) {
		String msg = "Database constraint violation: "
				+ (ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage());
		return build(HttpStatus.CONFLICT, "Conflict", msg, request);
	}

	// ── 404: SDR throws this when a repository.findById() returns empty ───────
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException ex, WebRequest request) {
		return build(HttpStatus.NOT_FOUND, "Not Found",
				ex.getMessage() != null ? ex.getMessage() : "Resource not found", request);
	}

	@ExceptionHandler(org.springframework.core.convert.ConversionFailedException.class)
	public ResponseEntity<Map<String, Object>> handleConversionError(
			org.springframework.core.convert.ConversionFailedException ex, WebRequest request) {

		String msg = "Invalid value provided. Expected a valid numeric ID.";

		return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, request);
	}

	// ── 400: illegal argument (e.g. composite id wrong format) ───────────────
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
	}

	@ExceptionHandler(org.springframework.beans.TypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTypeMismatchGeneral(
			org.springframework.beans.TypeMismatchException ex, WebRequest request) {

		String msg = "Type mismatch: please provide correct data type.";

		return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, request);
	}

	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(
			org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {

		String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

		return build(HttpStatus.BAD_REQUEST, "Validation Failed", msg, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex,
			WebRequest request) {

		String msg = "Invalid JSON format. Please check your request body.";

		return build(HttpStatus.BAD_REQUEST, "Bad Request. " + msg, ex.getMessage(), request);
	}

}
