/**
 * Field validator for building validation rules with a fluent API.
 */
package jazzyframework.http.validation;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class ValidatorField {
    private final Validator validator;
    private final String fieldName;
    
    /**
     * Creates a new field validator.
     * 
     * @param validator The parent validator
     * @param fieldName The field name
     */
    public ValidatorField(Validator validator, String fieldName) {
        this.validator = validator;
        this.fieldName = fieldName;
    }
    
    /**
     * Adds a required validation rule.
     * 
     * @return This field validator for method chaining
     */
    public ValidatorField required() {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return false;
                }
                if (value instanceof String) {
                    return !((String) value).isEmpty();
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field is required";
            }
        });
        return this;
    }
    
    /**
     * Adds a minimum length validation rule.
     * 
     * @param minLength The minimum length
     * @return This field validator for method chaining
     */
    public ValidatorField minLength(int minLength) {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return ((String) value).length() >= minLength;
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be at least " + minLength + " characters";
            }
        });
        return this;
    }
    
    /**
     * Adds a maximum length validation rule.
     * 
     * @param maxLength The maximum length
     * @return This field validator for method chaining
     */
    public ValidatorField maxLength(int maxLength) {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return ((String) value).length() <= maxLength;
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must not exceed " + maxLength + " characters";
            }
        });
        return this;
    }
    
    /**
     * Adds an email validation rule.
     * 
     * @return This field validator for method chaining
     */
    public ValidatorField email() {
        validator.addRule(fieldName, new ValidationRule() {
            private final Pattern EMAIL_PATTERN = Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
            );
            
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return EMAIL_PATTERN.matcher((String) value).matches();
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be a valid email address";
            }
        });
        return this;
    }
    
    /**
     * Adds a numeric validation rule.
     * 
     * @return This field validator for method chaining
     */
    public ValidatorField numeric() {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                
                if (value instanceof Number) {
                    return true;
                }
                
                if (value instanceof String) {
                    try {
                        Double.parseDouble((String) value);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                
                return false;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be numeric";
            }
        });
        return this;
    }
    
    /**
     * Adds a minimum value validation rule.
     * 
     * @param min The minimum value
     * @return This field validator for method chaining
     */
    public ValidatorField min(double min) {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                
                double numericValue;
                if (value instanceof Number) {
                    numericValue = ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        numericValue = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        return true; // Skip validation if value is not numeric
                    }
                } else {
                    return true; // Skip validation if value is not a number or string
                }
                
                return numericValue >= min;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be at least " + min;
            }
        });
        return this;
    }
    
    /**
     * Adds a maximum value validation rule.
     * 
     * @param max The maximum value
     * @return This field validator for method chaining
     */
    public ValidatorField max(double max) {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                
                double numericValue;
                if (value instanceof Number) {
                    numericValue = ((Number) value).doubleValue();
                } else if (value instanceof String) {
                    try {
                        numericValue = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                        return true; // Skip validation if value is not numeric
                    }
                } else {
                    return true; // Skip validation if value is not a number or string
                }
                
                return numericValue <= max;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must not exceed " + max;
            }
        });
        return this;
    }
    
    /**
     * Adds a pattern validation rule.
     * 
     * @param pattern The regex pattern
     * @param message The error message
     * @return This field validator for method chaining
     */
    public ValidatorField pattern(String pattern, String message) {
        validator.addRule(fieldName, new ValidationRule() {
            private final Pattern PATTERN = Pattern.compile(pattern);
            
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return PATTERN.matcher((String) value).matches();
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return message;
            }
        });
        return this;
    }
    
    /**
     * Adds a custom validation rule.
     * 
     * @param rule The validation rule
     * @return This field validator for method chaining
     */
    public ValidatorField custom(ValidationRule rule) {
        validator.addRule(fieldName, rule);
        return this;
    }
    
    /**
     * Moves to another field for validation.
     * 
     * @param fieldName The field name
     * @return A field validator for the new field
     */
    public ValidatorField field(String fieldName) {
        return validator.field(fieldName);
    }
    
    /**
     * Validates the data against the defined rules.
     * 
     * @return The validation result
     */
    public ValidationResult validate() {
        return validator.validate();
    }
    
    /**
     * Adds a URL validation rule.
     * 
     * @return This field validator for method chaining
     */
    public ValidatorField url() {
        validator.addRule(fieldName, new ValidationRule() {
            private final Pattern URL_PATTERN = Pattern.compile(
                "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
            );
            
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return URL_PATTERN.matcher((String) value).matches();
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be a valid URL";
            }
        });
        return this;
    }
    
    /**
     * Adds an alphanumeric validation rule.
     * 
     * @return This field validator for method chaining
     */
    public ValidatorField alphanumeric() {
        validator.addRule(fieldName, new ValidationRule() {
            private final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");
            
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    return ALPHANUMERIC_PATTERN.matcher((String) value).matches();
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must only contain letters and numbers";
            }
        });
        return this;
    }
    
    /**
     * Adds a date validation rule.
     * 
     * @param format The date format (e.g., "yyyy-MM-dd")
     * @return This field validator for method chaining
     */
    public ValidatorField date(String format) {
        validator.addRule(fieldName, new ValidationRule() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                if (value instanceof String) {
                    try {
                        LocalDate.parse((String) value, formatter);
                        return true;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                }
                return true;
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be a valid date in format " + format;
            }
        });
        return this;
    }
    
    /**
     * Adds a validation rule to check if the value is in a list of allowed values.
     * 
     * @param allowedValues The list of allowed values
     * @return This field validator for method chaining
     */
    public ValidatorField in(Object... allowedValues) {
        List<Object> values = Arrays.asList(allowedValues);
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                return values.contains(value);
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must be one of: " + String.join(", ", values.toString());
            }
        });
        return this;
    }
    
    /**
     * Adds a validation rule to check if the value is not in a list of disallowed values.
     * 
     * @param disallowedValues The list of disallowed values
     * @return This field validator for method chaining
     */
    public ValidatorField notIn(Object... disallowedValues) {
        List<Object> values = Arrays.asList(disallowedValues);
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                return !values.contains(value);
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must not be one of: " + String.join(", ", values.toString());
            }
        });
        return this;
    }
    
    /**
     * Adds a validation rule to check if the field value matches another field's value.
     * 
     * @param otherFieldName The name of the other field to compare with
     * @return This field validator for method chaining
     */
    public ValidatorField matches(String otherFieldName) {
        validator.addRule(fieldName, new ValidationRule() {
            @Override
            public boolean isValid(Object value) {
                if (value == null) {
                    return true; // Skip validation if value is null
                }
                
                Object otherValue = validator.getValue(otherFieldName);
                if (otherValue == null) {
                    return true; // Skip validation if other value is null
                }
                
                return value.equals(otherValue);
            }
            
            @Override
            public String getMessage() {
                return "The " + fieldName + " field must match the " + otherFieldName + " field";
            }
        });
        return this;
    }
} 