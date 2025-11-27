package co.unicauca.user.controller;

import co.unicauca.user.model.DepartmentHeadAssignment;
import co.unicauca.user.model.User;
import co.unicauca.user.repository.DepartmentHeadRepository;
import co.unicauca.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/department-head")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/assign")
    public ResponseEntity<?> assignDepartmentHead(@RequestParam Long teacherId,
                                                  @RequestParam String startDate,
                                                  @RequestParam String endDate,
                                                  @RequestHeader("X-User-Role") String userRole) {
        System.out.println("========== ASSIGN DEPARTMENT HEAD ==========");
        System.out.println("Teacher ID: " + teacherId);
        System.out.println("Start Date (string): " + startDate);
        System.out.println("End Date (string): " + endDate);
        System.out.println("User Role: " + userRole);
        
        if (!"COORDINATOR".equals(userRole)) {
            return ResponseEntity.status(403).body("Acceso denegado: Se requiere rol COORDINATOR");
        }

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        if (!teacher.isTeacher()) {
            return ResponseEntity.badRequest().body("El usuario asignado debe ser un profesor");
        }

        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate);
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate);
        
        System.out.println("Start Date (parsed): " + parsedStartDate);
        System.out.println("End Date (parsed): " + parsedEndDate);

        DepartmentHeadAssignment assignment = new DepartmentHeadAssignment(
                teacherId,
                parsedStartDate,
                parsedEndDate
        );

        DepartmentHeadAssignment saved = departmentHeadRepository.save(assignment);
        System.out.println("Asignación guardada con ID: " + saved.getId());
        System.out.println("===========================================");
        
        return ResponseEntity.ok("Jefe de departamento asignado exitosamente");
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentDepartmentHead() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("========== GET CURRENT DEPARTMENT HEAD ==========");
        System.out.println("Fecha/hora actual: " + now);
        
        java.util.List<DepartmentHeadAssignment> assignments = departmentHeadRepository.findCurrentDepartmentHeads(now);
        
        System.out.println("Asignaciones encontradas: " + assignments.size());
        
        if (!assignments.isEmpty()) {
            DepartmentHeadAssignment a = assignments.get(0); // Get the most recent one (ORDER BY id DESC)
            System.out.println("Asignación más reciente:");
            System.out.println("  - ID: " + a.getId());
            System.out.println("  - Teacher ID: " + a.getTeacherId());
            System.out.println("  - Start Date: " + a.getStartDate());
            System.out.println("  - End Date: " + a.getEndDate());
            
            User teacher = userRepository.findById(a.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
            
            System.out.println("  - Email: " + teacher.getEmail());
            System.out.println("================================================");
            
            return ResponseEntity.ok(teacher.getEmail());
        } else {
            System.out.println("No se encontró ninguna asignación activa");
            System.out.println("================================================");
            return ResponseEntity.notFound().build();
        }
    }
}
