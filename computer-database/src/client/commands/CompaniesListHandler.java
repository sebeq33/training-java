package client.commands;
import java.sql.SQLException;
import java.util.List;

import model.Company;
import service.Services;
import ui.UiConsole;

public class CompaniesListHandler implements ClientCommand {

	@Override
	public boolean runCommand(Services service, UiConsole ui, String[] args) {
		try {
			
			List<Company> companiesList = service.getCompanyService().getCompanyList();
			
			for (Company c : companiesList) {
				ui.write(c);
			}
			
		} catch (SQLException e) {
			ui.write(e);
		}
		
		return true;
	}
}
