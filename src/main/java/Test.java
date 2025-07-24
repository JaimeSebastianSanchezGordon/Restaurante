import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import modelo.JPA.JPAPlatoDAO;
import modelo.entity.Plato;
import modelo.dao.PlatoDAO;

public class Test {

	public static void main(String[] args) {
		EntityManager em = Persistence.createEntityManagerFactory("Restaurante").createEntityManager();

		Plato plato1 = new Plato(0, "Lomo saltado",
				"Tiras de carne de res salteadas con cebolla, tomate y papas fritas, acompañadas de arroz blanco.",
				8.50, 0);
		Plato plato2 = new Plato(0, "Ensalada con pollo",
				"Lechuga romana fresca con trozos de pechuga de pollo a la parrilla, crutones, queso parmesano y aderezo.",
				6.75, 0);
		Plato plato3 = new Plato(0, "Spaghetti a la boloñesa",
				"Pasta italiana con salsa de carne molida de res, tomate natural, especias y queso rallado.", 7.90, 0);

//		em.getTransaction().begin();
//		em.persist(plato3);
//		em.getTransaction().commit();

		System.out.println("TODOS LOS PLATOS");

		PlatoDAO plato = new JPAPlatoDAO();
		for (Plato p : plato.getPlatos()) {
			System.out.println(p);
		}

		System.out.println("BUSQUEDA DE PLATOS");

		for (Plato p : plato.getPlatosPorNombre("Ensaladaa")) {
			System.out.println(p);
		}

	}

}
