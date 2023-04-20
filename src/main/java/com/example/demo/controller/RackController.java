package com.example.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.RackCreateDTO;
import com.example.demo.controller.dto.RackDTO;
import com.example.demo.controller.dto.RackUpdateDTO;
import com.example.demo.exception.NoCompatibleRackException;
import com.example.demo.service.RackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/racks")
public class RackController {
  private final RackService service;

  @CrossOrigin
  @PreAuthorize("hasAuthority('SCOPE_rack.create')")
  @PostMapping
  public RackDTO create(@RequestBody @Validated RackCreateDTO dto)
      throws NoCompatibleRackException {
    return service.create(dto);
  }

  @CrossOrigin
  @PreAuthorize("hasAuthority('SCOPE_rack.read')")
  @GetMapping
  public List<RackDTO> read() {
    return service.read();
  }

  @PreAuthorize("hasAuthority('SCOPE_rack.update')")
  @PutMapping(path = "/{id}")
  public RackDTO update(
      @PathVariable(required = true) String id, @RequestBody @Validated RackUpdateDTO dto)
      throws NoCompatibleRackException {
    return service.update(id, dto);
  }

  @PreAuthorize("hasAuthority('SCOPE_rack.delete')")
  @DeleteMapping(path = "/{id}")
  public void delete(@PathVariable(required = true) String id) {
    service.delete(id);
  }
}
