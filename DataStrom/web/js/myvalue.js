 $(function () {

            $.ajax({

                type: "GET",

                url: "jsondata/startTime.json",

                dataType: "json",

                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
					  if(data[i].optionKey=="startTime")
					  {
						  var trTD =data[i].optionValue 
						  document.getElementById('startTime').innerHTML='启动时间:'+trTD;
					  }

                    }

                }

            });

        });