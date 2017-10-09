package client.commands;

import client.exceptions.CompanyDontExistException;
import client.tools.ArgsToComputer;
import model.Computer;
import service.Services;
import ui.UiConsole;

public class ComputerUpdateHandler implements ClientCommand {

	
	@Override
	public boolean runCommand(Services service, UiConsole ui, String[] args) throws Exception {
		Computer model = new ArgsToComputer().createComputerFromArgs(args);
		
		Long idCompany = model.getCompanyId();
		if (!service.getCompanyService().companyExists(idCompany))
		{
			throw new CompanyDontExistException(idCompany);
		} 
		
		service.getComputerService().updateComputer(model);
		
		ui.write(String.format("Computer \"%s\" has been updated (id: %d)", 
			model.getName(), model.getId()));
		return true;
	}
}
