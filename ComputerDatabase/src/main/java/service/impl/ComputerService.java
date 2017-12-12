package service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mapper.ComputerMapper;
import mapper.ComputerMapping;
import model.Company;
import model.Computer;
import model.pages.PageDto;
import persistence.IComputerDao;
import service.IComputerService;
import service.PageBuilder;

@Service("computerService")
@Transactional(readOnly = false)
public class ComputerService implements IComputerService {

    private IComputerDao  computerDao;
    private EntityManager em;

    /**
     * Private ctor.
     *
     * @param dao CompanyDao to access Data
     * @param em EntityManager
     */
    @Autowired
    private ComputerService(IComputerDao dao, EntityManager em) {
        this.computerDao = dao;
        this.em = em;
    }

    /**
     * @param id the computer id to search
     * @return the first computer corresponding exactly to @id
     * @throws SQLException content couldn't be loaded
     */
    @Override
    @Transactional(readOnly = true)
    public Computer getDetail(Long id) {
        Optional<Computer> one = computerDao.findById(id);
//        Hibernate.initialize(one); //EAGER LOAD COMPANY ID
        return one.get();
    }

    /**
     * @param newComputer complete computer to create, without id
     * @return the saved entity
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public Computer create(Computer newComputer) {

        return computerDao.save(setTransientCompanyFromId(newComputer));
    }

    /**
     * @param newComputer with transient(Hibernate) company
     * @return newComputer
     */
    private Computer setTransientCompanyFromId(Computer newComputer) {
        Company transientC = newComputer.getCompany();
        if (transientC != null) {
            transientC = em.getReference(Company.class, transientC.getId());
            newComputer.setCompany(transientC);
        }
        return newComputer;
    }

    /**
     * @param c full computer to update with id != null
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public void update(Computer c) {
        computerDao.save(setTransientCompanyFromId(c));
    }

    /**
     * @param id id of the computer to delete
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public void delete(Long id) {
        computerDao.deleteById(id);
    }

    /**
     * @param ids ids list of the computer to delete
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public void delete(List<Long> ids) {
        computerDao.deleteAllByIdIn(ids);
    }

    /**
     * @param request page request
     * @return the first page of the full computer preview list from DB
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public PageDto<Computer> loadPage(PageBuilder<Computer> request) {
        String search = request.getSearch() == null ?
            "" :
            request.getSearch();

        Pageable r = createPageRequest(request);
        Page<Computer> result = computerDao.findAllComputersByNameContainingOrCompanyNameContaining(r, search, search);
        Long total = result.getTotalElements();

        return request.build(result.getContent(), total);
    }

    /**
     * @param request pageBuilder
     * @return create spring PageRequest from PageBuilder
     */
    private Pageable createPageRequest(PageBuilder<Computer> request) {

        String order = request.getOrder();
        ComputerMapping columnSort = request.getColumnSort();
        Sort.Direction s = order == null || order.equals("ASC") ?
            Sort.Direction.ASC :
            Sort.Direction.DESC;
        String dbName = columnSort == null ?
            ComputerMapper.ID :
            columnSort.getDbName();

        return PageRequest.of(request.getNbPage().intValue() - 1, request.getPageSize().intValue(), s, dbName);
    }

    /**
     * @param search filter to search (computer or company name like) or null
     * @return number of elem
     * @throws SQLException fail to load
     */
    @Override
    @Transactional(readOnly = true)
    public Long getCount(String search) {
        return search == null ?
            computerDao.count() :
            computerDao.countByNameContainingOrCompanyNameContaining(search, search);
    }

    /**
     * @param id id of the company to delete computers from
     * @throws SQLException deletion failed
     */
    @Override
    public void deleteByCompany(Long id) {
        computerDao.deleteAllByCompanyId(id);
    }
}
