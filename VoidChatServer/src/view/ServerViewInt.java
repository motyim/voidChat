
package view;

import model.User;

/**
 *
 * @author MotYim
 */
public interface ServerViewInt {
    /**
     * 
     * @param username
     * @return User or null if not exsits
     */
    User getUserInfo(String username);
    
    /**
     * Send Announcement to all users 
     * @param message 
     */
    void sendAnnouncement(String message);
}
