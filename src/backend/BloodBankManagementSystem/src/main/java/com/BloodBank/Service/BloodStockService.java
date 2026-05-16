package com.BloodBank.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BloodBank.DTO.BloodStockResponseDTO;
import com.BloodBank.DTO.BloodStockSummaryDTO;
import com.BloodBank.Entity.BloodStock;
import com.BloodBank.Repository.BloodStockRepository;

@Service
public class BloodStockService {

    @Autowired
    private BloodStockRepository repo;

    // 🔹 Search by blood group + city (only valid stock)
    public List<BloodStockResponseDTO> search(String bloodGroup, String city) {

        if (bloodGroup == null || bloodGroup.isBlank()) {
            throw new RuntimeException("Blood group is required");
        }

        if (city == null || city.isBlank()) {
            throw new RuntimeException("City is required");
        }

        String bg = bloodGroup.toUpperCase().trim();
        String ct = city.trim();

        return repo
                .findByBloodGroupAndStorageCenter_CityAndExpiryDateAfter(
                        bg, ct, LocalDate.now())
                .stream()
                .map(stock -> mapToDTO(stock))
                .toList();
    }

    // 🔹 Search only by blood group (non-expired)
    public List<BloodStockResponseDTO> getByBloodGroup(String bloodGroup) {

        if (bloodGroup == null || bloodGroup.isBlank()) {
            throw new RuntimeException("Blood group is required");
        }

        String bg = bloodGroup.toUpperCase().trim();

        return repo
                .findByBloodGroupAndExpiryDateAfter(bg, LocalDate.now())
                .stream()
                .map(stock -> mapToDTO(stock))
                .toList();
    }

    // 🔹 Common mapper (clean code 🔥)
    private BloodStockResponseDTO mapToDTO(com.BloodBank.Entity.BloodStock stock) {

        BloodStockResponseDTO dto = new BloodStockResponseDTO();

        dto.setBloodGroup(stock.getBloodGroup());
        dto.setUnits(stock.getUnits());
        dto.setExpiryDate(stock.getExpiryDate());

        if (stock.getStorageCenter() != null) {
            dto.setCenterName(stock.getStorageCenter().getName());
            dto.setCity(stock.getStorageCenter().getCity());
        }

        return dto;
    }
    public List<BloodStockSummaryDTO> getAllStocks() {

        List<BloodStock> stocks = repo.findAll();

        // 🔥 Group by bloodGroup and sum units
        Map<String, Integer> grouped = stocks.stream()
                .collect(Collectors.groupingBy(
                        BloodStock::getBloodGroup,
                        Collectors.summingInt(BloodStock::getUnits)
                ));

        // 🔥 Convert to DTO list
        return grouped.entrySet().stream()
                .map(e -> new BloodStockSummaryDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}