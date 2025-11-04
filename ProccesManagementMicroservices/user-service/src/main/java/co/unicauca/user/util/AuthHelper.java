package co.unicauca.user.util;

import jakarta.servlet.http.HttpServletRequest;

public class AuthHelper {
    
    public static String getCurrentUserEmail(HttpServletRequest request) {
        return request.getHeader("X-User-Email");
    }
    
    public static String getCurrentUserRole(HttpServletRequest request) {
        return request.getHeader("X-User-Role");
    }
    
    public static boolean isAdmin(HttpServletRequest request) {
        return RoleConstants.ADMINISTRATOR.equals(getCurrentUserRole(request));
    }
    
    public static boolean isTeacher(HttpServletRequest request) {
        return RoleConstants.TEACHER.equals(getCurrentUserRole(request));
    }
    
    public static boolean isStudent(HttpServletRequest request) {
        return RoleConstants.STUDENT.equals(getCurrentUserRole(request));
    }
    
    public static void validateAdminRole(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new SecurityException("Acceso denegado: Se requiere rol " + RoleConstants.ADMINISTRATOR);
        }
    }
}