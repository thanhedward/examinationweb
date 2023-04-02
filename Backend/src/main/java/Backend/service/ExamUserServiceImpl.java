package Backend.service;

import Backend.entity.Exam;
import Backend.entity.ExamUser;
import Backend.entity.User;
import Backend.repository.ExamRepository;
import Backend.repository.ExamUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamUserServiceImpl implements ExamUserService {
    private ExamUserRepository examUserRepository;
    private ExamRepository examRepository;

    @Autowired
    public ExamUserServiceImpl(ExamUserRepository examUserRepository, ExamRepository examRepository) {
        this.examUserRepository = examUserRepository;
        this.examRepository = examRepository;
    }

    @Override
    public void create(Exam exam, List<User> userSet) {
        List<ExamUser> examUserList = new ArrayList<>();
        System.out.println("size: " + examUserList.size());
        userSet.forEach(user -> {
            ExamUser examUser = new ExamUser();
            examUser.setUser(user);
            examUser.setExam(exam);
            examUser.setRemainingTime(exam.getDurationExam() * 60);
            examUser.setTotalPoint(-1.0);
            examUserList.add(examUser);

        });
        examUserRepository.saveAll(examUserList);

    }

    @Override
    public List<ExamUser> getExamListByUsername(String username) {
        return examUserRepository.findAllByUser_UsernameAndExam_Canceled(username, false);
    }

    @Override
    public ExamUser findByExamAndUser(Long examId, String username) {
        return examUserRepository.findByExam_IdAndUser_Username(examId, username);
    }

    @Override
    public void update(ExamUser examUser) {
        examUserRepository.save(examUser);
    }

    @Override
    public Optional<ExamUser> findExamUserById(Long id) {
        return examUserRepository.findById(id);
    }

    @Override
    public List<ExamUser> getCompleteExams(Long courseId, String username) {
        List<ExamUser> examUserList = examUserRepository.findAllByExam_Part_Course_IdAndUser_UsernameAndTotalPointIsGreaterThan(courseId, username, -1.0);

        return examUserList;
    }

    @Override
    public List<ExamUser> findAllByExam_Id(Long examId) {
        return examUserRepository.findAllByExam_Id(examId);
    }

    @Override
    public List<ExamUser> findExamUsersByIsFinishedIsTrueAndExam_Id(Long examId) {
        return examUserRepository.findExamUsersByIsFinishedIsTrueAndExam_Id(examId);
    }
}
