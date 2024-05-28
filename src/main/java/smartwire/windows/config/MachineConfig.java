package smartwire.windows.config;

import smartwire.windows.model.MachineModel;
import smartwire.windows.presenter.MachinePresenter;
import smartwire.windows.view.MachineView;

public class MachineConfig {

    public MachineConfig(MachineView machineView) {
        machinePresenter(machineView);
    }

    private void machinePresenter(MachineView machineView) {
        MachinePresenter machinePresenter = new MachinePresenter(machineModel(), machineView);
        machineView.setMachinePresenter(machinePresenter);
    }
    private MachineModel machineModel() {return new MachineModel();}

}
