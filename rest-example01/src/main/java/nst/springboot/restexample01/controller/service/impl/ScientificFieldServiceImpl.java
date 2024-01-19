package nst.springboot.restexample01.controller.service.impl;

import nst.springboot.restexample01.controller.domain.EducationTitle;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.domain.ScientificField;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.repository.ScientificFieldRepository;
import nst.springboot.restexample01.controller.service.ScientificFieldService;
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
        Optional<ScientificField> check = scientificFieldRepository.findByScfField(name);
        if(check.isPresent()){
            throw new Exception("Scientific field " + name + " already exist!");
        }
        ScientificField scientificField = new ScientificField(scientificFieldRepository.count()+1,name);
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
                .orElseThrow(()->new Exception("Scientific field does not exist!"));
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
                .orElseThrow(()->new Exception("Scientific field does not exist!"));
        return scientificFieldConverter.toDto(scientificField);
    }

    @Override
    public void update(Long id, String newName) throws Exception {
        if(scientificFieldRepository.findByScfField(newName).isPresent()){throw new Exception("Scientific field "+ newName + " already exist!");}
        ScientificField scientificField = scientificFieldRepository.findById(id)
                .orElseThrow(()->new Exception("Scientific field with id " + id + " does not exist!"));
        scientificField.setScfField(newName);
        scientificFieldRepository.save(scientificField);
    }
}
