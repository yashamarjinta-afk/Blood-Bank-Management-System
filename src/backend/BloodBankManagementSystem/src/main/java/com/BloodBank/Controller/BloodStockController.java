package com.BloodBank.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BloodBank.DTO.BloodStockResponseDTO;
import com.BloodBank.DTO.BloodStockSummaryDTO;
import com.BloodBank.Entity.BloodStock;
import com.BloodBank.Repository.BloodStockRepository;
import com.BloodBank.Service.BloodStockService;

@RestController
@RequestMapping("/bloodstock")
public class BloodStockController {

    @Autowired
    private BloodStockService ser;
    
    @Autowired
    private BloodStockRepository stockrepo;

    // 🔹 Search by blood group + city
    @GetMapping("/search")
    public ResponseEntity<List<BloodStockResponseDTO>> search(
            @RequestParam String bloodGroup,
            @RequestParam String city) {

        List<BloodStockResponseDTO> res = ser.search(bloodGroup, city);
        return ResponseEntity.ok(res);
    }

    // 🔹 Get stock by blood group
    @GetMapping("/by-blood-group")
    public ResponseEntity<List<BloodStockResponseDTO>> getByBloodGroup(
            @RequestParam String bloodGroup) {

        List<BloodStockResponseDTO> res = ser.getByBloodGroup(bloodGroup);
        return ResponseEntity.ok(res);
    }
    
    @GetMapping("/getbloodstockbycenter/{id}")
    public List<BloodStockResponseDTO> getByCenter(@PathVariable Long id) {
    	
    	List<BloodStock> stocks=stockrepo.findByStorageCenterId(id);
    	
    	if(stocks.isEmpty()) {
    		throw new RuntimeException("No stock available in this center");
    	}
        return stocks
            .stream()
            .map(s -> new BloodStockResponseDTO(
                s.getBloodGroup(),
                s.getUnits()
            ))
            .toList();
    }
    
    @GetMapping("/all")
    public List<BloodStockSummaryDTO> getAllStocks() {
        return ser.getAllStocks();
    }
}