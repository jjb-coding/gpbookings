package mappers;

import java.util.ArrayList;

import api.output.DownloadDoctorsOutput;
import api.outputResult.DownloadDoctorsResult;
import mappers.output.DoctorDataList;
import services.mapperService.ClassPair;
import services.mapperService.Mapper;
import services.practiceProvider.DoctorData;

public class DownloadDoctorsOutputToDoctorDataMapper extends Mapper<DownloadDoctorsOutput, DoctorDataList> {
	public DoctorDataList invoke(DownloadDoctorsOutput in) {
		ArrayList<DoctorData> out = new ArrayList<>();
		for (DownloadDoctorsResult row : in.doctors()) {
			out.add(new DoctorData(
				row.UUID(),
				row.FirstName(),
				row.Surname(),
				row.Specialism()
				));
		}
		return new DoctorDataList(out);
	}

	@Override
	public ClassPair describe() {
		return new ClassPair(DownloadDoctorsOutput.class, DoctorDataList.class);
	}
}
