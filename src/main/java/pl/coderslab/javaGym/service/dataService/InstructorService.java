package pl.coderslab.javaGym.service.dataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.DomainObjectException;
import pl.coderslab.javaGym.error.customException.EmailSendingException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.model.Email;
import pl.coderslab.javaGym.repository.InstructorRepository;

import java.util.List;

@Service
public class InstructorService implements AbstractDataService<Instructor> {

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
    public Instructor findById(Long id) {
        return getInstructorByIdFromDB(id);
    }

    private Instructor getInstructorByIdFromDB(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElse(null);
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    @Transactional
    public Instructor save(Instructor instructor) {
        if (instructor.getId() == null) {
            if (!isInstructorEmailAlreadyInDB(instructor)) {
                return instructorRepository.save(instructor);
            } else {
                throw new DomainObjectException();
            }
        } else {
            throw new ActionNotAllowedException();
        }
    }

    @Transactional
    public Instructor edit(Instructor newInstructor, Long id) {
        Instructor instructorFromDB = getInstructorByIdFromDB(id);
        if (newInstructor.getEmail().equals(instructorFromDB.getEmail())) {
            newInstructor.setId(instructorFromDB.getId());
            return instructorRepository.save(newInstructor);
        } else {
            if (!isInstructorEmailAlreadyInDB(newInstructor)) {
                newInstructor.setId(instructorFromDB.getId());
                return instructorRepository.save(newInstructor);
            } else {
                throw new DomainObjectException();
            }
        }
    }

    private Boolean isInstructorEmailAlreadyInDB(Instructor instructor) {
        return instructorRepository.existsByEmail(instructor.getEmail());
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        Instructor instructor = getInstructorByIdFromDB(id);
        instructorRepository.delete(instructor);
        return true;
    }

    public List<Instructor> findByEmail(String email) {
        return instructorRepository.findAllByEmailIsContaining(email);
    }

    public List<Instructor> findByNames(String firstName, String lastName) {
        return instructorRepository
                .findAllByFirstNameIsContainingAndLastNameIsContaining(firstName, lastName);
    }

    @Transactional
    public Boolean sendEmailToInstructor(Email email, Long id) {
        try {
            Instructor instructor = getInstructorByIdFromDB(id);
            emailSender.sendEmailToPerson(instructor, email);
            return true;
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }
}
