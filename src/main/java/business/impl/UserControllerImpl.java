package business.impl;

import business.interfaces.UserController;
import business.ControllerRegistry;
import data.dbDTO.AgendaInfo;
import data.dbDTO.Course;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import data.mongoImpl.MongoUserDAO;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.util.*;

/**
 * Created by Christian on 08-06-2017.
 */
public class UserControllerImpl implements UserController {
    UserDAO userDAO = new MongoUserDAO();
    @Override
    public User getUserByCampusNetId(String cnId) throws ElementNotFoundException, PersistenceException {
        return userDAO.findByCampusNetId(cnId);
    }

    @Override
    public User saveUser(User user) throws PersistenceException {
        user.setUserName(user.getUserName().trim());
        return userDAO.save(user);
    }

    @Override
    public User getAnonymous() {
        User user = new User();
        user.setRoles(null);
        return user;
    }

    @Override
    public List<User> getAllUsers() throws PersistenceException {
        return userDAO.getAll();
    }

    @Override
    public List<Role> getUserRoles(String userId) throws ElementNotFoundException, PersistenceException {
        User user = userDAO.findByCampusNetId(userId);
        return user.getRoles();
    }

    @Override
    public List<Role> updateGlobalUserRoles(List<Role> roles, String userName) throws PersistenceException, ElementNotFoundException {
        int noUpdated = userDAO.findByFieldAndUpdateField("userName", userName, "roles", roles);
        if (noUpdated<=0) {throw new ElementNotFoundException("no user with that userName found");}
        else if (noUpdated==1) return roles;
        else {
            throw new PersistenceException("Multiple users with same ID found");
        }
    }

    @Override
    public User get(String userId) throws ValidException, PersistenceException {
        User user = userDAO.get(userId);
        Set<String> courseIds = user.getAgendaInfoMap().keySet();
        List<Course> multiCourses = ControllerRegistry.getCourseController().getMultiCourses(courseIds);
        Map<String, AgendaInfo> fetchedAgendaInfoMap = new HashMap<>();
        for (Course c : multiCourses) {
            AgendaInfo agendaInfo = new AgendaInfo();
            agendaInfo.setCourseName(c.getCourseShortHand() + " " + c.getCourseName());
            agendaInfo.setAgendaId(user.getAgendaInfoMap().get(c.getId()).getAgendaId());
            boolean containsAdmin = c.getAdmins().contains(userId);
            if(containsAdmin) {
                user.setAdminOfCourses(true);
                agendaInfo.setAdmin(true);
            }
            fetchedAgendaInfoMap.put(c.getId(),agendaInfo);
        }
        user.setAgendaInfoMap(fetchedAgendaInfoMap);
        return user;

    }

    @Override
    public Collection<User> getMultiple(Set<String> userIds) throws PersistenceException, ValidException {
        return userDAO.multiGet(userIds);

    }

    @Override
    public void removeAgenda(String userId, String courseId) throws ValidException, PersistenceException {
        User user = get(userId);
        Map<String, AgendaInfo> agendaInfoMap = user.getAgendaInfoMap();
        AgendaInfo agendaInfo = agendaInfoMap.get(courseId);
        ControllerRegistry.getAgendaController().deleteAgenda(agendaInfo.getAgendaId());
        user.getAgendaInfoMap().remove(courseId);
        user.setActiveAgenda(null);
        saveUser(user);
    }
}
