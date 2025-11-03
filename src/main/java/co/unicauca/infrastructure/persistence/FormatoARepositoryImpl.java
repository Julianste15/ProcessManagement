package co.unicauca.infrastructure.persistence;
import org.springframework.stereotype.Repository;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.entities.EstadoProyecto;
import co.unicauca.domain.entities.Modalidad;
import co.unicauca.domain.repositories.IFormatoARepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
@Repository
public class FormatoARepositoryImpl implements IFormatoARepository {
    private final SQLiteConnection db;

    // 4. CREAMOS LA CONEXIÓN (IGUAL QUE EN UserRepositoryImpl)
    public FormatoARepositoryImpl() {
        this.db = new SQLiteConnection();
    }

    // 5. IMPLEMENTAMOS LOS MÉTODOS DE LA INTERFAZ
    @Override
    public FormatoA save(FormatoA formatoA) {
        String sql = "INSERT INTO formato_a(titulo, modalidad, fecha_creacion, director_email, codirector_email, objetivo_general, archivo_pdf, estado, intentos) VALUES(?,?,?,?,?,?,?,?,?)";
        
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, formatoA.getTitulo());
            stmt.setString(2, formatoA.getModalidad().name());
            stmt.setString(3, formatoA.getFechaCreacion().toString());
            stmt.setString(4, formatoA.getDirector() != null ? formatoA.getDirector().getEmail() : null);
            stmt.setString(5, formatoA.getCodirector() != null ? formatoA.getCodirector().getEmail() : null);
            stmt.setString(6, formatoA.getObjetivoGeneral());
            stmt.setString(7, formatoA.getArchivoPDF());
            stmt.setString(8, formatoA.getEstado().name());
            stmt.setInt(9, formatoA.getIntentos());
            
            stmt.executeUpdate();
            
            return formatoA;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el Formato A", e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public Optional<FormatoA> findById(String titulo) {
        String sql = "SELECT * FROM formato_a WHERE titulo = ?";
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Usamos 'Optional.of()' para indicar que lo encontramos
                return Optional.of(mapRow(rs));
            } else {
                // Usamos 'Optional.empty()' para indicar que no se encontró
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Formato A por título", e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void update(FormatoA formatoA) {
        String sql = "UPDATE formato_a SET modalidad = ?, fecha_creacion = ?, director_email = ?, codirector_email = ?, objetivo_general = ?, archivo_pdf = ?, estado = ?, intentos = ? WHERE titulo = ?";
        
        db.connect();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, formatoA.getModalidad().name());
            stmt.setString(2, formatoA.getFechaCreacion().toString());
            stmt.setString(3, formatoA.getDirector() != null ? formatoA.getDirector().getEmail() : null);
            stmt.setString(4, formatoA.getCodirector() != null ? formatoA.getCodirector().getEmail() : null);
            stmt.setString(5, formatoA.getObjetivoGeneral());
            stmt.setString(6, formatoA.getArchivoPDF());
            stmt.setString(7, formatoA.getEstado().name());
            stmt.setInt(8, formatoA.getIntentos());
            stmt.setString(9, formatoA.getTitulo()); // El WHERE
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el Formato A", e);
        } finally {
            db.disconnect();
        }
    }
    
    // 6. FUNCIÓN 'mapRow' PARA CONVERTIR DE BD A OBJETO
    private FormatoA mapRow(ResultSet rs) throws SQLException {
        FormatoA formatoA = new FormatoA(
            rs.getString("titulo"),
            Modalidad.valueOf(rs.getString("modalidad")),
            LocalDate.parse(rs.getString("fecha_creacion")),
            null, // Para simplificar, no cargamos el User completo.
            null, // Esto se puede mejorar luego si es necesario.
            rs.getString("objetivo_general"),
            null, // Simplificación, no cargamos la lista de objetivos
            rs.getString("archivo_pdf")
        );
        formatoA.setEstado(EstadoProyecto.valueOf(rs.getString("estado")));
        formatoA.setIntentos(rs.getInt("intentos"));
        
        return formatoA;
    }
}
