package nst.springboot.restexample01.controller.service.impl;

import nst.springboot.restexample01.controller.domain.AcademicTitle;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.repository.AcademicTitleRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.service.AcademicTitleService;
import nst.springboot.restexample01.converter.impl.AcademicTitleConverter;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class AcademicTitleServiceImpl implements AcademicTitleService {
    private AcademicTitleRepository academicTitleRepository;
    private AcademicTitleConverter academicTitleConverter;
    private MemberRepository memberRepository;

    public AcademicTitleServiceImpl(AcademicTitleRepository academicTitleRepository,
                                    AcademicTitleConverter academicTitleConverter,
                                    MemberRepository memberRepository){
        this.academicTitleRepository = academicTitleRepository;
        this.academicTitleConverter = academicTitleConverter;
        this.memberRepository = memberRepository;
    }
    @Override
    @Transactional
    public AcademicTitleDto save(String name) throws Exception {
        Optional<AcademicTitle> check = academicTitleRepository.findByTitle(name);
        if(check.isPresent()){
            throw new Exception("Academic title " + name + " already exist!");
        }
        AcademicTitle academicTitle = new AcademicTitle(academicTitleRepository.count()+1,name);
        academicTitle = academicTitleRepository.save(academicTitle);
        return academicTitleConverter.toDto(academicTitle);
    }

    @Override
    public List<AcademicTitleDto> getAll() {
        return academicTitleRepository
                .findAll().stream().map(academicTitle -> academicTitleConverter.toDto(academicTitle))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        AcademicTitle academicTitle = academicTitleRepository.findById(id)
                .orElseThrow(()->new Exception("Academic title does not exist!"));
        List<Member> memberList = memberRepository.findByAcademicTitleId(id);
        if(memberList.isEmpty()) {
            academicTitleRepository.delete(academicTitle);
        }else{
            throw new Exception("Academic title can not be deleted, because there are members with that academic title!");
        }
    }

    @Override
    public AcademicTitleDto findById(Long id) throws Exception {
        AcademicTitle academicTitle = academicTitleRepository.findById(id)
                .orElseThrow(()->new Exception("Academic title does not exist!"));
        return academicTitleConverter.toDto(academicTitle);
    }

    @Override
    public void update(Long id, String newName) throws Exception {
        if(academicTitleRepository.findByTitle(newName).isPresent()){throw new Exception("Academic title "+ newName + " already exist!");}
        AcademicTitle academicTitle = academicTitleRepository.findById(id)
                .orElseThrow(()->new Exception("Academic title with id " + id + " does not exist!"));
        academicTitle.setTitle(newName);
        academicTitleRepository.save(academicTitle);
    }
}
