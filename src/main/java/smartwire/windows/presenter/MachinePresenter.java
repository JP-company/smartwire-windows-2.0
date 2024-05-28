package smartwire.windows.presenter;

import lombok.RequiredArgsConstructor;
import smartwire.windows.model.MachineModel;
import smartwire.windows.domain.Machine;
import smartwire.windows.view.MachineView;

@RequiredArgsConstructor
public class MachinePresenter {
    private final MachineModel machineModel;
    private final MachineView machineView;

    public void initMachine(Machine machine) {
        machineModel.connectRequest(machine);
    }
}
