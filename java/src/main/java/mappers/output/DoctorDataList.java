package mappers.output;

import java.util.ArrayList;

import services.practiceProvider.DoctorData;

public record DoctorDataList(ArrayList<DoctorData> list) {}
