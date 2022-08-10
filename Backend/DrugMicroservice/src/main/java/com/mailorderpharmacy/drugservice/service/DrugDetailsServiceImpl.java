package com.mailorderpharmacy.drugservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mailorderpharmacy.drugservice.dao.DrugDetailsRepository;
import com.mailorderpharmacy.drugservice.dao.DrugLocationRepository;
import com.mailorderpharmacy.drugservice.entity.DrugDetails;
import com.mailorderpharmacy.drugservice.entity.DrugLocationDetails;
import com.mailorderpharmacy.drugservice.entity.Stock;
import com.mailorderpharmacy.drugservice.entity.SuccessResponse;
import com.mailorderpharmacy.drugservice.entity.TokenValid;
import com.mailorderpharmacy.drugservice.exception.DrugNotFoundException;
import com.mailorderpharmacy.drugservice.exception.InvalidTokenException;
import com.mailorderpharmacy.drugservice.exception.StockNotFoundException;
import com.mailorderpharmacy.drugservice.restclients.AuthFeign;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DrugDetailsServiceImpl implements DrugDetailsService
{
    @Autowired
    private DrugDetailsRepository drugRepo;
    
    @Autowired
    private DrugLocationRepository locationRepo;
    
    @Autowired
    private AuthFeign authFeign;
    
    @Override
    public DrugDetails getDrugById(final String id, final String token) throws InvalidTokenException, DrugNotFoundException {
        log.info("Inside Service: getDrugById method");
    	DrugDetails drugDetails = null;
        if (((TokenValid)this.authFeign.getValidity(token).getBody()).isValid()) {
            drugDetails = this.drugRepo.findById(id).orElseThrow(() -> new DrugNotFoundException("Drug Not Found"));
            return drugDetails;
        }
        throw new InvalidTokenException("Invalid Credentials");
    }
    
    @Override
    public DrugDetails getDrugByName(final String name, final String token) throws InvalidTokenException, DrugNotFoundException {
    	log.info("Inside Service: getDrugByName method");
    	if (((TokenValid)this.authFeign.getValidity(token).getBody()).isValid()) {
        	return this.drugRepo.findBydrugName(name).orElseThrow(() -> new DrugNotFoundException("Drug Not Found"));
        }
        else
        	throw new InvalidTokenException("Invalid Credentials");
    }
    
    @Override
    public Stock getDispatchableDrugStock(final String id, final String location, final String token) throws InvalidTokenException, StockNotFoundException, DrugNotFoundException {
    	log.info("Inside Service: getDispatchableDrugStock method");
    	if (!((TokenValid)this.authFeign.getValidity(token).getBody()).isValid()) {
    		log.info("Token is invalid. Throwing InvalidTokenException.");
            throw new InvalidTokenException("Invalid Credentials");
        }
    	
        DrugDetails details = null;
        try {
            details = this.drugRepo.findById(id).get();
            log.info("Drug Detail fetched by id successfully");
        }
        catch (Exception e) {
        	log.info("Drug Detail not found by id. Throwing  DrugNotFoundException.");
            throw new DrugNotFoundException("Drug Not Found");
        }
        Stock stock = null;
        for (final DrugLocationDetails dld : details.getDruglocationQuantities()) {
            if (dld.getLocation().equalsIgnoreCase(location)) {
                stock = new Stock(id, details.getDrugName(), details.getExpiryDate(), dld.getQuantity());
                log.info("Stock found");
                break;
            }
        }
        if (stock == null) {
        	log.info("Stock not found. Throwing StockNotFoundException.");
            throw new StockNotFoundException("Stock Unavailable at your location");
        }
        
        log.info("Returning stock");
        return stock;
    }
    
    @Override
    public ResponseEntity<SuccessResponse> updateQuantity(final String drugName, final String location, final int quantity, final String token) throws InvalidTokenException, DrugNotFoundException, StockNotFoundException {
    	log.info("Inside Service: updateQuantity method");
    	if (!((TokenValid)this.authFeign.getValidity(token).getBody()).isValid()) {
    		log.info("Token is invalid. Throwing InvalidTokenException.");
            throw new InvalidTokenException("Invalid Credentials");
        }
        DrugDetails details = new DrugDetails();
        try {
            details = this.drugRepo.findBydrugName(drugName).get();
            log.info("Drug Detail fetched by name successfully");
            
        }
        catch (Exception e) {
        	log.info("Drug Detail not found by id. Throwing  DrugNotFoundException.");
            throw new DrugNotFoundException("Drug Not Found");
        }
        final List<DrugLocationDetails> dummy = 
        		details.getDruglocationQuantities()
        				.stream()
        				.filter(x -> x.getLocation().equalsIgnoreCase(location))
        				.collect(Collectors.toList());
        if (dummy.isEmpty()) {
        	log.info("Stock not found. Throwing StockNotFoundException.");
            throw new StockNotFoundException("Stock Unavailable at your location");
        }
        if (dummy.get(0).getQuantity() >= quantity) {
        	log.info("Stock is available");
            final DrugLocationDetails tempDetails = this.locationRepo.findByserialId(dummy.get(0).getSerialId()).get(0);
            final int val = tempDetails.getQuantity() - quantity;
            tempDetails.setQuantity(val);
            this.locationRepo.save(tempDetails);
            log.info("Drug quantity updated successfully");
            return new ResponseEntity<>(new SuccessResponse("Refill Done Successfully"), HttpStatus.OK);
        }
        log.info("Stock is not enough. Throwing StockNotFoundException.");
        throw new StockNotFoundException("Stock Unavailable at your location");
    }
    
    @Override
    public List<DrugDetails> getAllDrugs() {
    	log.info("Inside Service: getAllDrugs method");
        return this.drugRepo.findAll();
    }
}