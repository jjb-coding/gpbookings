package mappers;

import java.util.ArrayList;

import api.output.DownloadTitlesOutput;
import api.outputResult.DownloadTitlesTitleResult;
import mappers.output.TitleDataList;
import services.mapperService.ClassPair;
import services.mapperService.Mapper;
import services.practiceProvider.TitleData;

public class DownloadTitlesOutputToTitleDataMapper extends Mapper<DownloadTitlesOutput, TitleDataList> {
	public TitleDataList invoke(DownloadTitlesOutput in) {
		ArrayList<TitleData> out = new ArrayList<>();
		for (DownloadTitlesTitleResult row : in.titles()) {
			out.add(new TitleData(
				row.Identifier()
				));
		}
		return new TitleDataList(out);
	}
	
	@Override
	public ClassPair describe() {
		return new ClassPair(DownloadTitlesOutput.class, TitleDataList.class);
	}
}
