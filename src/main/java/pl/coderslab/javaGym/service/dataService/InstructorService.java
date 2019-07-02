package pl.coderslab.javaGym.service.dataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.error.customException.UniqueDBFieldException;
import pl.coderslab.javaGym.repository.InstructorRepository;

import java.util.List;

@Service
public class InstructorService {

    private InstructorRepository instructorRepository;
    private EmailSender emailSender;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, EmailSender emailSender) {
        this.instructorRepository = instructorRepository;
        this.emailSender = emailSender;
    }

    public List<Instructor> findAll() {
        return getInstructorListIfNotEmpty(instructorRepository.findAll());
    }

    public Instructor findInstructorById(Long id) {
        return getInstructorIfNotNull(instructorRepository.findById(id).orElse(null));
    }

    @Transactional
    public Instructor save(Instructor instructor) {
        if (instructor.getId() == null) {
            if (!isInstructorEmailAlreadyInDB(instructor)) {
                return instructorRepository.save(instructor);
            } else {
                throw new UniqueDBFieldException();
            }
        } else {
            throw new ActionNotAllowedException();
        }
    }

    @Transactional
    public Instructor edit(Instructor newInstructor, Long id) {
        Instructor instructorFromDB = findInstructorById(id);
        if (newInstructor.getEmail().equals(instructorFromDB.getEmail())) {
            newInstructor.setId(instructorFromDB.getId());
            return instructorRepository.save(newInstructor);
        } else {
            if (!isInstructorEmailAlreadyInDB(newInstructor)) {
                newInstructor.setId(instructorFromDB.getId());
                return instructorRepository.save(newInstructor);
            } else {
                throw new UniqueDBFieldException();
            }
        }
    }

    private Boolean isInstructorEmailAlreadyInDB(Instructor instructor) {
        return instructorRepository.existsByEmailIgnoreCase(instructor.getEmail());
    }

    @Transactional
    public Boolean deleteById(Long id) {
        Instructor instructor = findInstructorById(id);
        instructorRepository.delete(instructor);
        return true;
    }

    public List<Instructor> findByEmail(String email) {
        return getInstructorListIfNotEmpty(instructorRepository
                .findAllByEmailIsContainingIgnoreCase(email));
    }

    public List<Instructor> findByNames(String firstName, String lastName) {
        return getInstructorListIfNotEmpty(instructorRepository
                .findAllByFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
                        (firstName, lastName));
    }

    @Transactional
    public Boolean sendEmailToInstructor(EmailDto emailData, Long id) {
        Instructor instructor = findInstructorById(id);
        emailSender.sendEmailToPerson(instructor, emailData);
        return true;
    }

    private List<Instructor> getInstructorListIfNotEmpty(List<Instructor> instructors) {
        if (instructors.size() > 0) {
            return instructors;
        } else {
            throw new ResourceNotFoundException("*Instructors not found!");
        }
    }

    private Instructor getInstructorIfNotNull(Instructor instructor) {
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException("*Instructor not found!");
        }
    }
}
