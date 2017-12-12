package persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import model.Computer;

public interface IComputerDao extends JpaRepository<Computer, Long> {

    /**
     * @param ids List of computer ids to delete
     */
    void deleteAllByIdIn(List<Long> ids);

    /**
     * @param id company_id
     */
    void deleteAllByCompanyId(Long id);

    /**
     * @param name name
     * @param companyName name
     * @return count
     */
    Long countByNameContainingOrCompanyNameContaining(String name, String companyName);

    //
    // /**
    // * @param page content needed
    // * @param name computer name
    // * @param companyName companyName
    // * @return page
    // */
    // @Query("SELECT co FROM Computer co join co.company ca WHERE "
    // + "(LOWER(co.name) LIKE LOWER( %:name% )) OR "
    // + "(LOWER(ca.name) LIKE LOWER( %:companyName% ))")
    // Page<Computer> findAllComputersBySearch(Pageable page, @Param(value = "name")
    // String name, @Param(value = "companyName") String companyName);
    //
    //
//
//    /**
//     * @param name name
//     * @param pageable content needed
//     * @return page
//     */
//    @Query(value = "SELECT co.* FROM computer co where LOWER(co.name) LIKE LOWER(CONCAT('%',?1,'%')) ORDER BY ?#{#pageable}", countQuery = "SELECT count(*) FROM computer co where LOWER(co.name) LIKE LOWER(CONCAT('%',?1,'%'))", nativeQuery = true)
//    Page<Computer> findSearch(String name, Pageable pageable);
//

     /**
     * @param page content needed
     * @param name computer name
     * @param companyName companyName
     * @return page
     */
    Page<Computer> findAllComputersByNameContainingOrCompanyNameContaining(Pageable page, String name,
            String companyName);

    /**
     * @param page content needed
     * @param name computer name
     * @return page
     */
    Page<Computer> findAllComputersByNameContaining(Pageable page, String name);
}