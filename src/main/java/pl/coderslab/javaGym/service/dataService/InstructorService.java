package pl.coderslab.javaGym.service.dataService;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.DomainObjectException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
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

    @Override
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

    private Instructor getInstructorByIdFromDB(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElse(null);
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) {

    }
}
