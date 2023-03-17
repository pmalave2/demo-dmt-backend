package com.example.demo.service.impl;

import com.example.demo.controller.dto.WarehouseCreateDTO;
import com.example.demo.controller.dto.WarehouseDTO;
import com.example.demo.controller.dto.WarehouseUpdateDTO;
import com.example.demo.entity.RackEntity;
import com.example.demo.entity.RackEntity.Type;
import com.example.demo.exception.NoFoundWarehouseException;
import com.example.demo.mapper.WarehouseMapper;
import com.example.demo.repository.RackRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.WarehouseService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {
  private final WarehouseRepository warehouseRepository;
  private static final WarehouseMapper mapper = WarehouseMapper.INSTANCE;
  private final RackRepository rackRepository;

  @Override
  public WarehouseDTO create(WarehouseCreateDTO dto) {
    var entity = mapper.dtoToEntity(dto);
    var entityPersisted = warehouseRepository.save(entity);

    return mapper.entityToDto(entityPersisted);
  }

  @Override
  public List<WarehouseDTO> read() {
    return warehouseRepository.findAll().stream().map(mapper::entityToDto).toList();
  }

  @Override
  public WarehouseDTO readById(String id) {
    var entityWarehouseOptional = warehouseRepository.findById(id);

    if (!entityWarehouseOptional.isPresent()) throw new NoFoundWarehouseException();

    return mapper.entityToDto(entityWarehouseOptional.get());
  }

  @Override
  public WarehouseDTO update(String id, WarehouseUpdateDTO dto) {
    var entityWarehouseOptional = warehouseRepository.findById(id);

    if (!entityWarehouseOptional.isPresent()) throw new NoFoundWarehouseException();

    var entity = entityWarehouseOptional.get();
    entity.setFamily(dto.getFamily());
    entity.setSize(dto.getSize());
    entity.setClient(dto.getClient());

    return mapper.entityToDto(warehouseRepository.save(entity));
  }

  @Override
  public void delete(String id) {
    warehouseRepository.deleteById(id);
  }

  @Override
  public List<List<String>> permutate(String id) {
    var entityWarehouseOptional = warehouseRepository.findById(id);

    if (!entityWarehouseOptional.isPresent()) throw new NoFoundWarehouseException();

    var racks = rackRepository.findAllByWarehouseId(id);

    var types = racks.stream().map(RackEntity::getType).map(Type::toString).toList();

    return permutate(types);
  }

  public static <T> List<List<T>> permutate(List<T> src) {
    if (src.size() <= 1) {
      List<List<T>> al = new ArrayList<>();
      al.add(src);
      return al;
    }

    List<List<T>> result = new ArrayList<>();
    for (int i = 0; i < src.size(); i++) {
      List<T> copy = new ArrayList<>(src);
      T el = copy.remove(i);

      for (List<T> perm : permutate(copy)) {
        List<T> c = new ArrayList<>(perm);
        c.add(0, el);
        result.add(c);
      }
    }

    return result;
  }
}
