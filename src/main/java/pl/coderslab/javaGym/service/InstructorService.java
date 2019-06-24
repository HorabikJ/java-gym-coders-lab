package pl.coderslab.javaGym.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.coderslab.javaGym.email.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.error.customException.DomainObjectException;
import pl.coderslab.javaGym.repository.InstructorRepository;

import java.util.List;

@Service
public class InstructorService implements AbstractStandardService<Instructor> {

    private InstructorRepository instructorRepository;
    private EmailSender emailSender;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, EmailSender emailSender) {
        this.instructorRepository = instructorRepository;
        this.emailSender = emailSender;
    }

    @Override
    public List<Instructor> findAll() {
        return null;
    }

    @Override
    public Instructor findById(Long Id) {
        return null;
    }

    @Override
    public Instructor save(Instructor instructor) {
        try {
            if (instructor.getId() == null) {
//                emailSender.sendEmail(instructor, EmailTypeEnum.WELCOME_INSTR_EMAIL);
            }
            return instructorRepository.save(instructor);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DomainObjectException();
        }
    }

    @Override
    public void deleteById(Long id) {

    }
}
