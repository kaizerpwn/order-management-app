package com.ibrahimokic.ordermanagement.controller;

import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.mapper.impl.AddressMapperImpl;
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200" })
@Tag(name = "Address", description = "Operations related to addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapperImpl addressMapper;

    @GetMapping
    @Operation(summary = "Get all addresses", description = "Get list of all addresses")
    @ApiResponse(responseCode = "200", description = "List of addresses", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Address.class)))
    })
    public ResponseEntity<?> getAllAddresses() {
        try {
            List<AddressDto> retrievedAddresses = addressMapper.mapListToDtoList(addressService.getAllAddresses());
            return ResponseEntity.ok(retrievedAddresses);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Get a address by ID", description = "Get a address by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))),
            @ApiResponse(responseCode = "404", description = "Address not found.")
    })
    public ResponseEntity<?> getAddressById(@PathVariable Long addressId) {
        try {
            Optional<Address> address = addressService.getAddressById(addressId);

            if (address.isPresent()) {
                return ResponseEntity.ok(addressMapper.mapTo(address.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("Address not found.");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new address", description = "Create new address based on request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> createAddress(@RequestBody @Valid AddressDto addressDto, BindingResult bindingResult) {
        ResponseEntity<?> errors = Utils.getBindingResults(bindingResult);
        if (errors != null)
            return errors;

        try {
            Address newAddress = addressMapper.mapFrom(addressDto);
            Address createdAddress = addressService.createAddress(newAddress);
            return ResponseEntity.status(HttpStatus.CREATED).body(addressMapper.mapTo(createdAddress));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Internal server error");
        }
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete address", description = "Delete address with provided address id")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        try {
            boolean deletionResult = addressService.deleteAddress(addressId);

            if (deletionResult) {
                return ResponseEntity.ok().body("Address deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("An error occurred while deleting address " + addressId + ".");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit address", description = "Edit address based on request body and address ID")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId,
            @Valid @RequestBody AddressDto updatedAddressDto, BindingResult bindingResult) {
        ResponseEntity<?> errors = Utils.getBindingResults(bindingResult);
        if (errors != null)
            return errors;

        try {
            AddressDto updatedAddress = addressService.updateAddress(addressId, updatedAddressDto);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedAddress);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }
}
