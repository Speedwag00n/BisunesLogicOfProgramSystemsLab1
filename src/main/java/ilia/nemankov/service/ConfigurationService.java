package ilia.nemankov.service;

import ilia.nemankov.model.Configuration;

import java.util.Date;

public interface ConfigurationService {

    boolean isConfigurationAvailable(Configuration configuration, Date arrivalDate, Date departureDate);

}
