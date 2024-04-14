package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.domain.Member;
import nst.springboot.restexample01.domain.ScientificField;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
import nst.springboot.restexample01.service.ScientificFieldService;
import nst.springboot.restexample01.converter.impl.ScientificFieldConverter;
import nst.springboot.restexample01.dto.ScientificFieldDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScientificFieldServiceImpl implements ScientificFieldService {
    private ScientificFieldRepository scientificFieldRepository;
    private ScientificFieldConverter scientificFieldConverter;
    private MemberRepository memberRepository;

    public ScientificFieldServiceImpl(ScientificFieldRepository scientificFieldRepository,
                                      ScientificFieldConverter scientificFieldConverter,
                                      MemberRepository memberRepository) {
        this.scientificFieldRepository = scientificFieldRepository;
        this.scientificFieldConverter = scientificFieldConverter;
        this.memberRepository = memberRepository;
    }

    @Override
    public ScientificFieldDto save(String name) throws Exception {
        Optional<ScientificField> check = scientificFieldRepository.findByScfFieldIgnoreCase(name);
        if(check.isPresent()){
            throw new EntityExistsException("Scientific field " + name + " already exist!");
        }
        ScientificField scientificField = new ScientificField(scientificFieldRepository.findMaxId()+1,name);
        scientificField = scientificFieldRepository.save(scientificField);
        return scientificFieldConverter.toDto(scientificField);

    }

    @Override
    public List<ScientificFieldDto> getAll() {
        return scientificFieldRepository
                .findAll().stream().map(scientificField -> scientificFieldConverter.toDto(scientificField))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        ScientificField scientificField = scientificFieldRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Scientific field does not exist!"));
        List<Member> memberList = memberRepository.findByScientificFieldId(id);
        if(memberList.isEmpty()) {
            scientificFieldRepository.delete(scientificField);
        }else{
            throw new Exception("Scientific field can not be deleted, because there are members from that scientific field!");
        }
    }

    @Override
    public ScientificFieldDto findById(Long id) throws Exception {
        ScientificField scientificField = scientificFieldRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Scientific field does not exist!"));
        return scientificFieldConverter.toDto(scientificField);
    }

    @Override
    public void update(Long id, String newName) throws Exception {
        if(scientificFieldRepository.findByScfFieldIgnoreCase(newName).isPresent()){throw new EntityExistsException("Scientific field "+ newName + " already exist!");}
        ScientificField scientificField = scientificFieldRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Scientific field with id " + id + " does not exist!"));
        scientificField.setScfField(newName);
        scientificFieldRepository.save(scientificField);
    }
}
