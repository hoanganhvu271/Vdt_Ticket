package com.hav.vt_ticket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hav.vt_ticket.R;

public class GuideFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_guide, container, false);

        TextView information1 = view.findViewById(R.id.tv_guide_1);
        TextView information2 = view.findViewById(R.id.tv_guide_2);

        String content1 =
                "• Để lên xe buýt, bạn phải xuất trình vé điện tử cho nhân viên xe buýt và nhận vé giấy từ hãng xe buýt.\n\n" +
                "• Vé điện tử của Viettel Tikcet sẽ được xuất sau khi chúng tôi xác nhận khoản thanh toán của bạn.\n\n" +
                "• Bạn có thể sử dụng vé điện tử được gửi cho bạn qua email, SMS hoặc trong Ứng dụng Viettel Tikcet của bạn.\n\n" +
                "• Vào ngày khởi hành, hãy mang theo vé điện tử và giấy tờ tùy thân của bạn đến điểm lên xe (quầy ở nhà ga, văn phòng đại lý, hoặc nơi khác).\n" +
                "• Thời gian khởi hành, loại xe buýt và tuyến xe buýt có thể thay đổi mà không cần thông báo trước vì lý do hoạt động.\n\n" ;

        String content2 = "• Vé khởi hành trong giai đoạn Cuối năm và Tết Nguyên đán có thể không được hoàn, đổi lịch. Vui lòng xác nhận ngày khởi hành của bạn trước khi thực hiện thanh toán.\n\n" +
                "• Có mặt tại văn phòng/quầy vé/bến xe trước 30 phút để làm thủ tục lên xe.\n\n" +
                "• Xuất trình SMS/Email đặt vé trước khi lên xe.\n\n" +
                "• Không mang đồ ăn, thức ăn có mùi lên xe.\n\n" +
                "• Không hút thuốc, uống rượu, sử dụng chất kích thích trên xe.\n\n" +
                "• Không mang các vật dễ cháy nổ lên xe\n\n" +
                "• Không vứt rác trên xe\n\n" +
                "• Không làm ồn, gây mất trật tự trên xe\n\n" +
                "• Không mang giày, dép trên xe\n\n" +
                "• Tổng trọng lượng hành lý không vượt quá 3 kg\n\n" +
                "• Trẻ em dưới 5 tuổi hoặc dưới 110 cm được miễn phí vé nếu ngồi cùng ghế/giường với bố mẹ\n\n" +
                "• Trẻ em từ 5 tuổi hoặc cao từ 110 cm trở lên mua vé như người lớn\n\n" +
                "• Trẻ em dưới 5 cm được miễn phí vé nếu ngồi cùng ghế/giường với bố mẹ\n\n" +
                "• Trẻ em từ 5 tuổi trở lên mua vé như người lớn\n\n" +
                "• Hãng xe chỉ chấp nhận vận chuyển động vật như là một hành lý ký gửi; không cho phép mang lên xe cùng hành khách.\n\n" +
                "• Viettel Ticket Customer Service: 03825197***";

        information1.setText(content1);
        information2.setText(content2);

        return view;
    }
}
